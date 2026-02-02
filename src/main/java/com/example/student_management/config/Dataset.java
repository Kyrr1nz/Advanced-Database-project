package com.example.student_management.config;

import com.example.student_management.entity.*;
import com.example.student_management.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.*;

@Configuration
public class Dataset {

    @Bean
    CommandLineRunner loadData(StudentRepository sRepo, MajorRepository mRepo,
                               ClassRepository cRepo, SubjectRepository subRepo,
                               TeacherRepository tRepo, CourseSectionRepository csRepo,
                               EnrollmentRepository eRepo, ExamRepository exRepo) {
        return args -> {
            Random rand = new Random();

            // 1. Chuyên ngành
            String[] majorNames = {"Công nghệ thông tin", "Kỹ thuật phần mềm", "An toàn thông tin", "Quản trị kinh doanh", "Kế toán", "Marketing"};
            List<Major> majors = new ArrayList<>();
            for (String name : majorNames) {
                Major m = new Major(); m.setMajorName(name);
                majors.add(mRepo.save(m));
            }

            // 2. Lớp hành chính & 3. Giảng viên
            List<ClassEntity> adminClasses = new ArrayList<>();
            for (Major m : majors) {
                String shortName = getShortName(m.getMajorName());
                for (char suffix = 'A'; suffix <= 'C'; suffix++) {
                    ClassEntity c = new ClassEntity();
                    c.setClassName(shortName + "-K15" + suffix);
                    c.setMajor(m);
                    adminClasses.add(cRepo.save(c));
                }
            }

            String[] tNames = {"TS. Lê Nam", "ThS. Minh Hà", "PGS. Hoài An", "TS. Quốc Bảo", "ThS. Thu Trang", "TS. Văn Dũng"};
            List<Teacher> teachers = new ArrayList<>();
            for (String tn : tNames) {
                Teacher t = new Teacher(); t.setFullName(tn);
                t.setEmail(tn.toLowerCase().replaceAll("[^a-z]", "") + "@university.edu.vn");
                teachers.add(tRepo.save(t));
            }

            // 4. Môn học
            String[] subNames = {"Lập trình Java", "Cấu trúc dữ liệu", "Cơ sở dữ liệu", "Kinh tế vi mô", "Pháp luật đại cương", "Tiếng Anh chuyên ngành", "Mạng máy tính", "Hệ điều hành", "Phân tích hệ thống"};
            List<Subject> subjects = new ArrayList<>();
            for (String sn : subNames) {
                Subject s = new Subject(); s.setSubjectName(sn); s.setCredits(3);
                subjects.add(subRepo.save(s));
            }

            // 5. TẠO LỚP HỌC PHẦN THEO 3 NĂM (2024, 2025, 2026)
            List<CourseSection> allSections = new ArrayList<>();
            int[] years = {2024, 2025, 2026};

            for (int year : years) {
                Map<String, LocalDate[]> timeline = new LinkedHashMap<>();
                timeline.put("Học kỳ 1", new LocalDate[]{LocalDate.of(year, 1, 2), LocalDate.of(year, 4, 15)});
                timeline.put("Học kỳ 2", new LocalDate[]{LocalDate.of(year, 5, 15), LocalDate.of(year, 8, 25)});
                timeline.put("Học kỳ 3", new LocalDate[]{LocalDate.of(year, 9, 20), LocalDate.of(year, 12, 15)});

                for (Map.Entry<String, LocalDate[]> entry : timeline.entrySet()) {
                    String semName = entry.getKey();
                    LocalDate start = entry.getValue()[0];
                    LocalDate end = entry.getValue()[1];

                    for (Subject s : subjects) {
                        for (int j = 1; j <= 3; j++) { // Mỗi môn 3 lớp/kỳ để tránh quá tải DB
                            CourseSection sec = new CourseSection();
                            sec.setSubject(s);
                            sec.setTeacher(teachers.get(rand.nextInt(teachers.size())));
                            sec.setSemester(semName);
                            sec.setCourse_year(year); // Thiết lập năm học tương ứng
                            sec.setStartDate(start);
                            sec.setEndDate(end);
                            allSections.add(csRepo.save(sec));
                        }
                    }
                }
            }

            // 6. Nạp 2000 SV & Đăng ký ngẫu nhiên qua các năm
            String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Vũ", "Phan", "Đỗ", "Bùi", "Lý"};
            String[] lottên = {"Thế", "Minh", "Thị", "Văn", "Gia", "Bảo", "Ngọc", "Hoàng", "Phương", "Anh"};
            String[] tens = {"An", "Bình", "Chi", "Dũng", "Em", "Giang", "Hương", "Khánh", "Linh", "Minh", "Tâm", "Hùng", "Sơn"};

            List<Student> currentBatch = new ArrayList<>();
            for (int i = 1; i <= 2000; i++) {
                Student s = new Student();
                s.setMssv(String.format("SV%04d", i));
                s.setFullName(hos[rand.nextInt(hos.length)] + " " + lottên[rand.nextInt(lottên.length)] + " " + tens[rand.nextInt(tens.length)]);
                s.setGender(rand.nextBoolean() ? "Nam" : "Nữ");
                s.setEmail("student" + i + "@uni.edu.vn");
                s.setClazz(adminClasses.get(rand.nextInt(adminClasses.size())));
                currentBatch.add(s);

                if (i % 200 == 0 || i == 2000) {
                    List<Student> savedStudents = sRepo.saveAll(currentBatch);
                    List<Enrollment> enrolls = new ArrayList<>();
                    for (Student st : savedStudents) {
                        Set<Long> pickedSubjectIds = new HashSet<>();
                        // Tăng lên 8 môn để trải đều ra 3 năm cho đẹp
                        while (pickedSubjectIds.size() < 8) {
                            CourseSection randomSec = allSections.get(rand.nextInt(allSections.size()));
                            if (!pickedSubjectIds.contains(randomSec.getSubject().getId())) {
                                pickedSubjectIds.add(randomSec.getSubject().getId());
                                Enrollment en = new Enrollment();
                                en.setStudent(st);
                                en.setCourseSection(randomSec);
                                en.setEnrollmentDate(randomSec.getStartDate().minusDays(10));
                                en.setStatus("Đã hoàn thành");
                                enrolls.add(en);
                            }
                        }
                    }
                    eRepo.saveAll(enrolls);
                    currentBatch.clear();
                    System.out.print(".");
                }
            }

            // 7. LỊCH THI: CÁCH 2 TUẦN SAU KẾT THÚC MÔN
            for (CourseSection sec : allSections) {
                Exam ex = new Exam();
                ex.setCourseSection(sec);
                // Ngày thi = Ngày kết thúc môn + 14 ngày (2 tuần)
                ex.setExamDate(sec.getEndDate().plusDays(14 + rand.nextInt(3)));
                ex.setRoom("Phòng " + (100 + rand.nextInt(100)));
                exRepo.save(ex);
            }

            System.out.println("\n>>> NẠP XONG: Dữ liệu cho trường Đại Học");
        };
    }

    private String getShortName(String majorName) {
        StringBuilder sb = new StringBuilder();
        for (String word : majorName.split(" ")) {
            if (!word.isEmpty()) sb.append(word.charAt(0));
        }
        return sb.toString().toUpperCase();
    }
}
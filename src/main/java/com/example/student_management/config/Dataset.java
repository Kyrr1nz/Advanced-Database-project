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

            // 1. CHUYÊN NGÀNH
            String[] majorNames = {"Công nghệ thông tin", "Kỹ thuật phần mềm", "An toàn thông tin", "Quản trị kinh doanh", "Kế toán", "Marketing"};
            List<Major> majors = new ArrayList<>();
            for (String name : majorNames) {
                Major m = new Major();
                m.setMajorName(name);
                majors.add(mRepo.save(m));
            }

            // 2. LỚP HÀNH CHÍNH
            List<ClassEntity> adminClasses = new ArrayList<>();
            for (Major m : majors) {
                String shortName = getShortName(m.getMajorName());
                for (char suffix = 'A'; suffix <= 'B'; suffix++) { // Giảm xuống 2 lớp/ngành (A, B)
                    ClassEntity c = new ClassEntity();
                    c.setClassName(shortName + "-K15" + suffix);
                    c.setMajor(m);
                    adminClasses.add(cRepo.save(c));
                }
            }

            // 3. GIẢNG VIÊN
            String[] tNames = {"TS. Lê Nam", "ThS. Minh Hà", "PGS. Hoài An", "TS. Quốc Bảo", "ThS. Thu Trang", "TS. Văn Dũng"};
            List<Teacher> teachers = new ArrayList<>();
            for (String tn : tNames) {
                Teacher t = new Teacher();
                t.setFullName(tn);
                t.setEmail(tn.toLowerCase().replaceAll("[^a-z]", "") + "@university.edu.vn");
                teachers.add(tRepo.save(t));
            }

            // 4. MÔN HỌC
            String[] subNames = {"Lập trình Java", "Cấu trúc dữ liệu", "Cơ sở dữ liệu", "Kinh tế vi mô", "Pháp luật đại cương", "Tiếng Anh chuyên ngành", "Mạng máy tính", "Hệ điều hành", "Phân tích hệ thống"};
            List<Subject> subjects = new ArrayList<>();
            for (String sn : subNames) {
                Subject s = new Subject();
                s.setSubjectName(sn);
                s.setCredits(3);
                subjects.add(subRepo.save(s));
            }

            // 5. TẠO LỚP HỌC PHẦN (Giảm số lượng xuống cho nhẹ máy)
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
                        // Mỗi môn chỉ tạo 1 lớp mỗi kỳ để tránh trùng lặp quá nhiều (Tổng: 3*3*9 = 81 lớp)
                        CourseSection sec = new CourseSection();
                        sec.setSubject(s);
                        sec.setTeacher(teachers.get(rand.nextInt(teachers.size())));
                        sec.setSemester(semName);
                        sec.setCourse_year(year);
                        sec.setStartDate(start);
                        sec.setEndDate(end);
                        allSections.add(csRepo.save(sec));
                    }
                }
            }

            // 6. NẠP 2000 SINH VIÊN & ĐĂNG KÝ HỌC
            String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Vũ", "Phan", "Đỗ", "Bùi", "Lý"};
            String[] lottên = {"Thế", "Minh", "Thị", "Văn", "Gia", "Bảo", "Ngọc", "Hoàng", "Phương", "Anh"};
            String[] tens = {"An", "Bình", "Chi", "Dũng", "Em", "Giang", "Hương", "Khánh", "Linh", "Minh", "Tâm", "Hùng", "Sơn"};
            String[] grades = {"A", "B+", "B", "C+", "C", "D+", "D", "F"};

            List<Student> currentBatch = new ArrayList<>();
            for (int i = 1; i <= 2000; i++) {
                Student s = new Student();
                s.setMssv(String.format("SV%04d", i));
                s.setFullName(hos[rand.nextInt(hos.length)] + " " + lottên[rand.nextInt(lottên.length)] + " " + tens[rand.nextInt(tens.length)]);
                s.setGender(rand.nextBoolean() ? "Nam" : "Nữ");
                s.setEmail("student" + i + "@uni.edu.vn");
                s.setClazz(adminClasses.get(rand.nextInt(adminClasses.size())));

                int birthYear = 2004 + rand.nextInt(3); // Sinh năm 2004 - 2006
                s.setDob(LocalDate.of(birthYear, 1 + rand.nextInt(12), 1 + rand.nextInt(28)));

                currentBatch.add(s);

                if (i % 200 == 0 || i == 2000) {
                    List<Student> savedStudents = sRepo.saveAll(currentBatch);
                    List<Enrollment> enrolls = new ArrayList<>();

                    for (Student st : savedStudents) {
                        Set<Long> pickedSubjectIds = new HashSet<>();
                        // Đăng ký ngẫu nhiên 6 môn cho mỗi SV
                        while (pickedSubjectIds.size() < 6) {
                            CourseSection randomSec = allSections.get(rand.nextInt(allSections.size()));
                            if (!pickedSubjectIds.contains(randomSec.getSubject().getSubject_id())) {
                                pickedSubjectIds.add(randomSec.getSubject().getSubject_id());

                                Enrollment en = new Enrollment();
                                en.setStudent(st);
                                en.setCourseSection(randomSec);
                                en.setEnrollmentDate(randomSec.getStartDate().minusDays(rand.nextInt(20) + 1));

                                // Nếu lớp đã kết thúc thì có điểm, nếu chưa thì "Đang học"
                                if (randomSec.getEndDate().isBefore(LocalDate.now())) {
                                    en.setStatus("Đã hoàn thành");
                                    // Giả định bạn có field Grade trong Enrollment
                                    // en.setGrade(grades[rand.nextInt(grades.length)]);
                                } else {
                                    en.setStatus("Đang học");
                                }
                                enrolls.add(en);
                            }
                        }
                    }
                    eRepo.saveAll(enrolls);
                    currentBatch.clear();
                    System.out.print(".");
                }
            }

            // 7. LỊCH THI
            for (CourseSection sec : allSections) {
                Exam ex = new Exam();
                ex.setCourseSection(sec);
                ex.setExamDate(sec.getEndDate().plusDays(7 + rand.nextInt(7)));
                ex.setRoom("Phòng " + (100 + rand.nextInt(100)));
                exRepo.save(ex);
            }

            System.out.println("\n>>> NẠP XONG DỮ LIỆU HỆ THỐNG!");
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
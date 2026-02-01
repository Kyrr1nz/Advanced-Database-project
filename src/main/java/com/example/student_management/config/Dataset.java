package com.example.student_management.config;

import com.example.student_management.entity.*;
import com.example.student_management.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class Dataset {

    @Bean
    CommandLineRunner loadData(StudentRepository sRepo, MajorRepository mRepo,
                               ClassRepository cRepo, SubjectRepository subRepo,
                               TeacherRepository tRepo, CourseSectionRepository csRepo,
                               EnrollmentRepository eRepo, ExamRepository exRepo) {
        return args -> {
            // 1. Tạo Ngành học đa dạng
            Major it = saveMajor(mRepo, "Công nghệ thông tin");
            Major biz = saveMajor(mRepo, "Quản trị kinh doanh");
            Major mkt = saveMajor(mRepo, "Marketing");
            Major log = saveMajor(mRepo, "Logistics & Chuỗi cung ứng");
            Major design = saveMajor(mRepo, "Thiết kế đồ họa");

            // 2. Tạo Lớp hành chính theo khóa K15
            ClassEntity c1 = saveClass(cRepo, "IT-K15", it);
            ClassEntity c2 = saveClass(cRepo, "BA-K15", biz);
            ClassEntity c3 = saveClass(cRepo, "MKT-K15", mkt);
            ClassEntity c4 = saveClass(cRepo, "LOG-K15", log);
            ClassEntity c5 = saveClass(cRepo, "DS-K15", design);

            // 3. Tạo Giảng viên với học vị
            Teacher t1 = saveTeacher(tRepo, "TS. Nguyễn Văn Bình");
            Teacher t2 = saveTeacher(tRepo, "ThS. Lê Thị Lan");
            Teacher t3 = saveTeacher(tRepo, "PGS.TS Hoàng Nam");
            Teacher t4 = saveTeacher(tRepo, "ThS. Đặng Thu Thảo");

            // 4. Tạo Môn học
            Subject sub1 = saveSubject(subRepo, "Lập trình Java nâng cao");
            Subject sub2 = saveSubject(subRepo, "Cơ sở dữ liệu MySQL");
            Subject sub3 = saveSubject(subRepo, "Kỹ năng giao tiếp");
            Subject sub4 = saveSubject(subRepo, "Kinh tế vi mô");

            // 5. Tạo Lớp học phần (Course Sections)
            CourseSection secJava = saveSection(csRepo, sub1, t1, "2023-02-08", "2023-05-08");
            CourseSection secSQL = saveSection(csRepo, sub2, t2, "2023-03-01", "2023-06-01");
            CourseSection secSoftSkill = saveSection(csRepo, sub3, t4, "2023-02-15", "2023-05-15");

            // 6. Tạo 50 Sinh viên với đầy đủ thông tin
            String[] hos = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Vũ", "Phan", "Đỗ", "Bùi", "Đặng"};
            String[] dem = {"Văn", "Thị", "Duy", "Minh", "Thu", "Hồng", "Thanh", "Anh"};
            String[] tens = {"Khang", "Nam", "Triết", "Bưởi", "Thái", "An", "Bình", "Chi", "Dũng", "Hà", "Linh", "Quân"};
            Random rand = new Random();
            List<ClassEntity> classes = List.of(c1, c2, c3, c4, c5);

            for (int i = 1; i <= 50; i++) {
                Student s = new Student();
                s.setMssv(String.format("SV%03d", i));

                String gender = rand.nextBoolean() ? "Nam" : "Nữ";
                String hoTen = hos[rand.nextInt(hos.length)] + " " + dem[rand.nextInt(dem.length)] + " " + tens[rand.nextInt(tens.length)];

                s.setFullName(hoTen);
                s.setGender(gender); // Tự động gán giới tính
                s.setEmail("sv" + i + "@university.edu.vn");
                s.setPhoneNumber("09" + (10000000 + rand.nextInt(90000000))); // SĐT ngẫu nhiên
                s.setClazz(classes.get(rand.nextInt(classes.size())));

                sRepo.save(s);

                // Đăng ký học phần chéo (Mỗi SV học 2 môn ngẫu nhiên)
                eRepo.save(new Enrollment(s, secSoftSkill)); // Môn kỹ năng mềm ai cũng học
                if (i % 2 == 0) eRepo.save(new Enrollment(s, secJava));
                else eRepo.save(new Enrollment(s, secSQL));
            }

            // 7. Tạo Lịch thi cho các môn
            saveExam(exRepo, secJava, "2023-05-15", "P.402-A1");
            saveExam(exRepo, secSQL, "2023-06-10", "P.Lab-02");
            saveExam(exRepo, secSoftSkill, "2023-05-20", "Hội trường G");

            System.out.println(">>> Hệ thống Khang Á: Đã nạp 50 sinh viên đa ngành và lịch thi!");
        };
    }

    // --- CÁC HÀM TRỢ GIÚP (HELPERS) ---
    private Major saveMajor(MajorRepository repo, String name) {
        Major m = new Major(); m.setMajorName(name); return repo.save(m);
    }
    private ClassEntity saveClass(ClassRepository repo, String name, Major m) {
        ClassEntity c = new ClassEntity(); c.setClassName(name); c.setMajor(m); return repo.save(c);
    }
    private Teacher saveTeacher(TeacherRepository repo, String name) {
        Teacher t = new Teacher(); t.setFullName(name); return repo.save(t);
    }
    private Subject saveSubject(SubjectRepository repo, String name) {
        Subject s = new Subject(); s.setSubjectName(name); return repo.save(s);
    }
    private CourseSection saveSection(CourseSectionRepository repo, Subject s, Teacher t, String start, String end) {
        CourseSection sec = new CourseSection();
        sec.setSubject(s); sec.setTeacher(t); sec.setSemester("Học kỳ 2");
        sec.setStartDate(LocalDate.parse(start)); sec.setEndDate(LocalDate.parse(end));
        return repo.save(sec);
    }
    private void saveExam(ExamRepository repo, CourseSection sec, String date, String room) {
        Exam ex = new Exam(); ex.setCourseSection(sec);
        ex.setExamDate(LocalDate.parse(date)); ex.setRoom(room);
        repo.save(ex);
    }
}
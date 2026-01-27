package com.example.student_management.config;

import com.example.student_management.entity.*;
import com.example.student_management.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.Random;

@Configuration
public class Dataset {

    @Bean
    CommandLineRunner loadData(StudentRepository sRepo, MajorRepository mRepo,
                               ClassRepository cRepo, SubjectRepository subRepo,
                               TeacherRepository tRepo, CourseSectionRepository csRepo,
                               EnrollmentRepository eRepo, ExamRepository exRepo) {
        return args -> {
            // 1. Tạo Ngành học
            Major it = new Major(); it.setMajorName("Công nghệ thông tin"); mRepo.save(it);
            Major biz = new Major(); biz.setMajorName("Quản trị kinh doanh"); mRepo.save(biz);
            Major mkt = new Major(); mkt.setMajorName("Marketing"); mRepo.save(mkt);

            // 2. Tạo Lớp hành chính
            ClassEntity classIT = new ClassEntity(); classIT.setClassName("IT-K15"); classIT.setMajor(it); cRepo.save(classIT);
            ClassEntity classBA = new ClassEntity(); classBA.setClassName("BA-K15"); classBA.setMajor(biz); cRepo.save(classBA);
            ClassEntity classMKT = new ClassEntity(); classMKT.setClassName("MKT-K15"); classMKT.setMajor(mkt); cRepo.save(classMKT);

            // 3. Tạo Giảng viên & Môn học
            Teacher t1 = new Teacher(); t1.setFullName("Thầy Bình Vaadin"); tRepo.save(t1);
            Teacher t2 = new Teacher(); t2.setFullName("Cô Lan MySQL"); tRepo.save(t2);

            Subject sub1 = new Subject(); sub1.setSubjectName("Lập trình Java"); subRepo.save(sub1);
            Subject sub2 = new Subject(); sub2.setSubjectName("Cơ sở dữ liệu"); subRepo.save(sub2);

            // 4. Tạo Lớp học phần (Course Sections)
            CourseSection secJava = new CourseSection();
            secJava.setSubject(sub1); secJava.setTeacher(t1); secJava.setSemester("Học kỳ 2");
            secJava.setStartDate(LocalDate.of(2023, 2, 8)); secJava.setEndDate(LocalDate.of(2023, 5, 8));
            csRepo.save(secJava);

            CourseSection secSQL = new CourseSection();
            secSQL.setSubject(sub2); secSQL.setTeacher(t2); secSQL.setSemester("Học kỳ 2");
            secSQL.setStartDate(LocalDate.of(2023, 3, 1)); secSQL.setEndDate(LocalDate.of(2023, 6, 1));
            csRepo.save(secSQL);

            // 5. Vòng lặp tạo 30 Sinh viên và Đăng ký chéo
            String[] ho = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Vũ", "Phan"};
            String[] ten = {"Khang", "Nam", "Triết", "Bưởi", "Thái", "An", "Bình", "Chi", "Dũng", "Hà"};
            Random rand = new Random();

            for (int i = 1; i <= 30; i++) {
                Student s = new Student();
                s.setMssv(String.format("SV%03d", i));
                s.setFullName(ho[rand.nextInt(ho.length)] + " " + ten[rand.nextInt(ten.length)]);
                s.setEmail("sv" + i + "@example.com");

                // Chia sinh viên vào các lớp khác nhau
                if (i <= 10) s.setClazz(classIT);
                else if (i <= 20) s.setClazz(classBA);
                else s.setClazz(classMKT);

                sRepo.save(s);

                // LOGIC ĐĂNG KÝ: Cho các ngành học chung với nhau
                // SV 1-20 học Java (Gồm IT và Kinh doanh học chung)
                if (i <= 20) {
                    eRepo.save(new Enrollment(s, secJava));
                }
                // SV 11-30 học MySQL (Gồm Kinh doanh và Marketing học chung)
                if (i >= 11) {
                    eRepo.save(new Enrollment(s, secSQL));
                }
            }
            System.out.println(">>> Hệ thống Khang Á: Đã nạp 30 sinh viên và đăng ký học phần chéo!");
        };
    }
}
package com.example.student_management.ui;

import com.example.student_management.entity.Exam;
import com.example.student_management.entity.CourseSection;
import com.example.student_management.entity.Supervisor;
import com.example.student_management.service.ExamService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("exams")
@PageTitle("Exam Schedule | SMS")
public class ExamListView extends VerticalLayout {

    public ExamListView(ExamService examService) {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2("Lịch thi học kỳ"));
        add(new Button("⬅ Back to Dashboard", e -> UI.getCurrent().navigate(""))); // Điều hướng về trang chủ

        Grid<Exam> grid = new Grid<>(Exam.class, false);

        // 🎯 Cột TÊN MÔN (Lấy từ Subject thông qua CourseSection)
        grid.addColumn(exam -> {
            CourseSection cs = exam.getCourseSection();
            return (cs != null && cs.getSubject() != null) ? cs.getSubject().getSubjectName() : "N/A";
        }).setHeader("Môn học").setSortable(true).setAutoWidth(true);

        // 👨‍🏫 Cột GIÁM THỊ (Mới thêm dựa trên quan hệ Manage)
        grid.addColumn(exam -> {
            Supervisor sv = exam.getSupervisor();
            return (sv != null) ? sv.getsName() : "Chưa phân công";
        }).setHeader("Giám thị").setSortable(true).setAutoWidth(true);

        // 📅 Ngày thi
        grid.addColumn(Exam::getExamDate)
                .setHeader("Ngày thi").setSortable(true);

        // 🏫 Phòng
        grid.addColumn(Exam::getRoom)
                .setHeader("Phòng thi").setSortable(true);

        // Nạp dữ liệu từ Service
        grid.setItems(examService.getAllExams());

        // ✅ CLICK ROW → SANG TRANG DETAIL
        grid.addItemClickListener(event -> {
            Long examId = event.getItem().getExamId();
            UI.getCurrent().navigate("exam-detail/" + examId);
        });

        add(grid);
        expand(grid); // Giúp Grid chiếm trọn không gian còn lại
    }
}
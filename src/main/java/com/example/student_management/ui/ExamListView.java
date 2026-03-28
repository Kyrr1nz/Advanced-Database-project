package com.example.student_management.ui;

import com.example.student_management.entity.Exam;
import com.example.student_management.entity.CourseSection;
import com.example.student_management.service.ExamService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("exams")
public class ExamListView extends VerticalLayout {

    public ExamListView(ExamService examService) {
        add(new Button("⬅ Back to Dashboard", e -> UI.getCurrent().navigate(MainView.class)));
        setSizeFull();

        Grid<Exam> grid = new Grid<>(Exam.class, false);

        // 🎯 Cột TÊN MÔN
        grid.addColumn(exam -> {
            CourseSection cs = exam.getCourseSection();
            if (cs == null || cs.getSubject() == null) return "N/A";
            return cs.getSubject().getSubjectName();
        }).setHeader("Course").setSortable(true);

        // 📅 Ngày thi
        grid.addColumn(Exam::getExamDate)
                .setHeader("Date").setSortable(true);

        // 🏫 Phòng
        grid.addColumn(Exam::getRoom)
                .setHeader("Room").setSortable(true);

        grid.setItems(examService.getAllExams());

        // ✅ CLICK ROW → SANG TRANG DETAIL
        grid.addItemClickListener(event -> {
            Long examId = event.getItem().getExamId();
            UI.getCurrent().navigate("exam-detail/" + examId);
        });

        add(grid);
    }
}
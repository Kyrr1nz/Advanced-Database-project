package com.example.student_management.ui;

import com.example.student_management.entity.*;
import com.example.student_management.service.ExamService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;

import java.util.List;
import java.util.stream.Collectors;

@Route("exam-detail/:id")
@PageTitle("Chi tiết Lịch thi")
public class ExamDetailView extends VerticalLayout implements BeforeEnterObserver {

    private final ExamService examService;

    private final VerticalLayout infoContainer = new VerticalLayout();
    private final Grid<Student> studentGrid = new Grid<>(Student.class, false);

    public ExamDetailView(ExamService examService) {
        this.examService = examService;

        setSpacing(true);
        setPadding(true);
        setSizeFull(); // 🔥 QUAN TRỌNG

        // 🔹 NAV BAR
        HorizontalLayout navRow = new HorizontalLayout();
        navRow.setWidthFull();
        navRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Button backBtn = new Button("Quay lại", new Icon(VaadinIcon.ARROW_BACKWARD));
        backBtn.addClickListener(e ->
                UI.getCurrent().getPage().executeJs("window.history.back();")
        );

        Button homeBtn = new Button("Trang chủ", new Icon(VaadinIcon.HOME));
        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        homeBtn.addClickListener(e -> UI.getCurrent().navigate(""));

        navRow.add(backBtn, homeBtn);

        // 🔹 TITLE
        H2 title = new H2("Chi tiết Lịch thi");

        add(navRow, title, infoContainer, studentGrid);

        configureGrid();
    }

    private void configureGrid() {
        studentGrid.addColumn(Student::getMssv)
                .setHeader("MSSV")
                .setAutoWidth(true).setSortable(true);

        studentGrid.addColumn(Student::getFullName)
                .setHeader("Họ tên")
                .setAutoWidth(true).setSortable(true);

        studentGrid.addColumn(s ->
                s.getClazz() != null ? s.getClazz().getClassName() : "N/A"
        ).setHeader("Lớp");

        studentGrid.addColumn(s ->
                (s.getClazz() != null && s.getClazz().getMajor() != null)
                        ? s.getClazz().getMajor().getMajorName()
                        : "N/A"
        ).setHeader("Ngành");

        studentGrid.setSizeFull(); // 🔥 FULL CHIỀU CAO
        studentGrid.getStyle().set("cursor", "pointer");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long id = Long.parseLong(event.getRouteParameters().get("id").get());

        Exam exam = examService.getExamById(id);
        removeAll();

        CourseSection cs = exam.getCourseSection();

        // 🔹 NAV + TITLE (phải add lại vì removeAll)
        HorizontalLayout navRow = new HorizontalLayout();
        navRow.setWidthFull();
        navRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Button backBtn = new Button("Quay lại", new Icon(VaadinIcon.ARROW_BACKWARD));
        backBtn.addClickListener(e ->
                UI.getCurrent().getPage().executeJs("window.history.back();")
        );

        Button homeBtn = new Button("Trang chủ", new Icon(VaadinIcon.HOME));
        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        homeBtn.addClickListener(e -> UI.getCurrent().navigate(""));

        navRow.add(backBtn, homeBtn);

        H2 title = new H2("Chi tiết Lịch thi");

        // 🔹 INFO
        infoContainer.removeAll();
        infoContainer.setPadding(false);

        String subject = cs.getSubject() != null ? cs.getSubject().getSubjectName() : "N/A";
        String teacher = cs.getTeacher() != null ? cs.getTeacher().getFullName() : "Chưa có";
        String room = exam.getRoom();
        String date = exam.getExamDate().toString();

        infoContainer.add(
                new Span("📖 Môn học: " + subject),
                new Span("👨‍🏫 Giảng viên coi thi: " + teacher),
                new Span("🏫 Phòng thi: " + room),
                new Span("📅 Ngày thi: " + date),
                new Span("👥 Sĩ số: " + (cs.getEnrollments() != null ? cs.getEnrollments().size() : 0))
        );

        // 🔹 STUDENT LIST
        if (cs.getEnrollments() != null) {
            List<Student> students = cs.getEnrollments()
                    .stream()
                    .map(Enrollment::getStudent)
                    .collect(Collectors.toList());

            studentGrid.setItems(students);
        }

        add(navRow, title, infoContainer, studentGrid);

        expand(studentGrid); // 🔥 GRID CHIẾM HẾT PHẦN CÒN LẠI
    }
}
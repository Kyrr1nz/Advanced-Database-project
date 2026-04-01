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
        event.getRouteParameters().get("id").ifPresent(idStr -> {
            Long id = Long.parseLong(idStr);
            Exam exam = examService.getExamById(id);

            if (exam != null) {
                CourseSection cs = exam.getCourseSection();

                // 🔹 Cập nhật thông tin chi tiết
                infoContainer.removeAll();
                infoContainer.setPadding(false);

                String subject = (cs != null && cs.getSubject() != null) ? cs.getSubject().getSubjectName() : "N/A";

                // LẤY GIÁM THỊ TỪ EXAM (Thay vì Teacher từ CourseSection)
                String supervisorName = (exam.getSupervisor() != null) ? exam.getSupervisor().getsName() : "Chưa phân công";

                String room = exam.getRoom();
                String date = (exam.getExamDate() != null) ? exam.getExamDate().toString() : "N/A";
                int count = (cs != null && cs.getEnrollments() != null) ? cs.getEnrollments().size() : 0;

                infoContainer.add(
                        new H3("Thông tin chung"),
                        new Span("📖 Môn học: " + subject),
                        new Span("👨‍🏫 Giám thị coi thi: " + supervisorName), // Đã sửa ở đây
                        new Span("🏫 Phòng thi: " + room),
                        new Span("📅 Ngày thi: " + date),
                        new Span("👥 Sĩ số đăng ký: " + count + " sinh viên")
                );

                // 🔹 Cập nhật danh sách sinh viên
                if (cs != null && cs.getEnrollments() != null) {
                    List<Student> students = cs.getEnrollments()
                            .stream()
                            .map(Enrollment::getStudent)
                            .collect(Collectors.toList());

                    studentGrid.setItems(students);
                }
            } else {
                // Nếu không tìm thấy Exam, quay về trang danh sách
                event.forwardTo(ExamListView.class);
            }
        });
    }
}
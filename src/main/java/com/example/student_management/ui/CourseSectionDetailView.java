package com.example.student_management.ui;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.entity.Student;
import com.example.student_management.repository.CourseSectionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route("course-section-detail")
@PageTitle("Danh sách Lớp học phần")
public class CourseSectionDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final CourseSectionRepository csRepo;
    private final VerticalLayout infoContainer = new VerticalLayout();
    private final Grid<Student> studentGrid = new Grid<>(Student.class, false);

    public CourseSectionDetailView(CourseSectionRepository csRepo) {
        this.csRepo = csRepo;
        setSpacing(true);
        setPadding(true);

        // 1. Tạo thanh điều hướng (Navigation Row)
        HorizontalLayout navRow = new HorizontalLayout();
        navRow.setWidthFull();
        navRow.setJustifyContentMode(JustifyContentMode.BETWEEN); // Đẩy 2 nút ra 2 đầu

        // Nút Quay lại (Bên trái) - Sử dụng lịch sử trình duyệt
        Button returnBtn = new Button("Quay lại", new Icon(VaadinIcon.ARROW_BACKWARD));
        returnBtn.addClickListener(e ->
                UI.getCurrent().getPage().executeJs("window.history.back();")
        );

        // Nút Trang chủ (Bên phải) - Quay về MainView
        Button homeBtn = new Button("Trang chủ", new Icon(VaadinIcon.HOME));
        homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        homeBtn.addClickListener(e -> UI.getCurrent().navigate(""));

        navRow.add(returnBtn, homeBtn);

        // 2. Tiêu đề trang
        H2 title = new H2("Chi tiết Lớp học phần");

        add(navRow, title, infoContainer, studentGrid);

        configureGrid();
    }

    private void configureGrid() {
        studentGrid.addColumn(Student::getMssv).setHeader("MSSV").setAutoWidth(true).setSortable(true);
        studentGrid.addColumn(Student::getFullName).setHeader("Họ Tên").setAutoWidth(true).setSortable(true);
        studentGrid.addColumn(s -> s.getClazz() != null ? s.getClazz().getClassName() : "N/A")
                .setHeader("Lớp hành chính");
        studentGrid.addColumn(s -> (s.getClazz() != null && s.getClazz().getMajor() != null)
                        ? s.getClazz().getMajor().getMajorName() : "N/A")
                .setHeader("Chuyên ngành");

        studentGrid.addItemClickListener(event -> {
            Long studentId = event.getItem().getId();
            UI.getCurrent().navigate(StudentProfileView.class, studentId);
        });

        studentGrid.getStyle().set("cursor", "pointer");
        studentGrid.setAllRowsVisible(true);
    }

    @Override
    public void setParameter(BeforeEvent event, Long sectionId) {
        csRepo.findByIdWithStudents(sectionId).ifPresent(section -> {
            infoContainer.removeAll();
            infoContainer.setPadding(false);

            String subName = section.getSubject() != null ? section.getSubject().getSubjectName() : "N/A";
            String teaName = section.getTeacher() != null ? section.getTeacher().getFullName() : "Chưa phân công";

            infoContainer.add(
                    new Span("📖 Môn học: " + subName),
                    new Span("👨‍🏫 Giảng viên: " + teaName),
                    new Span("📅 Thời gian: " + section.getStartDate() + " ⮕ " + section.getEndDate()),
                    new Span("👥 Sĩ số: " + (section.getEnrollments() != null ? section.getEnrollments().size() : 0))
            );

            if (section.getEnrollments() != null) {
                List<Student> students = section.getEnrollments().stream()
                        .map(Enrollment::getStudent)
                        .collect(Collectors.toList());
                studentGrid.setItems(students);
            }
        });
    }
}
package com.example.student_management.ui;

import com.example.student_management.entity.Student;
import com.example.student_management.service.ClassService;
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

@Route("class-detail")
@PageTitle("Chi tiết Lớp học")
public class ClassDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final ClassService classService;
    private final VerticalLayout container = new VerticalLayout();

    public ClassDetailView(ClassService classService) {
        this.classService = classService;

        setSpacing(true);
        setPadding(true);

        add(container);
        container.setPadding(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Long classId) {
        container.removeAll();

        classService.findById(classId).ifPresent(classEntity -> {

            // 1. THANH ĐIỀU HƯỚNG (Navigation Row): Trái (Quay lại) - Phải (Trang chủ)
            HorizontalLayout navRow = new HorizontalLayout();
            navRow.setWidthFull();
            navRow.setJustifyContentMode(JustifyContentMode.BETWEEN); // Đẩy nút về 2 đầu

            // Nút Quay lại (Bên trái)
            Button returnBtn = new Button("Quay lại", new Icon(VaadinIcon.ARROW_BACKWARD));
            returnBtn.addClickListener(e ->
                    UI.getCurrent().getPage().executeJs("window.history.back();")
            );

            // Nút Trang chủ (Bên phải)
            Button homeBtn = new Button("Trang chủ", new Icon(VaadinIcon.HOME));
            homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            homeBtn.addClickListener(e -> UI.getCurrent().navigate(""));

            navRow.add(returnBtn, homeBtn);
            container.add(navRow);

            // 2. TIÊU ĐỀ VÀ THÔNG TIN LỚP
            H2 title = new H2("Lớp: " + classEntity.getClassName());

            Span majorInfo = new Span("Chuyên ngành: " +
                    (classEntity.getMajor() != null ? classEntity.getMajor().getMajorName() : "N/A"));
            majorInfo.getStyle().set("color", "#64748b");
            majorInfo.getStyle().set("margin-top", "-10px");

            container.add(title, majorInfo);

            // 3. BẢNG DANH SÁCH SINH VIÊN
            Grid<Student> studentGrid = new Grid<>(Student.class, false);

            studentGrid.addColumn(Student::getMssv).setHeader("MSSV").setAutoWidth(true).setSortable(true);
            studentGrid.addColumn(Student::getFullName).setHeader("Họ và Tên").setAutoWidth(true).setSortable(true);
            studentGrid.addColumn(Student::getEmail).setHeader("Email").setAutoWidth(true);

            studentGrid.addItemClickListener(clickEvent -> {
                Long studentId = clickEvent.getItem().getId();
                UI.getCurrent().navigate(StudentProfileView.class, studentId);
            });

            studentGrid.getStyle().set("cursor", "pointer");
            studentGrid.setItems(classEntity.getStudents());
            studentGrid.setAllRowsVisible(true);

            container.add(studentGrid);
        });
    }
}
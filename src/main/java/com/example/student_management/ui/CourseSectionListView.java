package com.example.student_management.ui;

import com.example.student_management.entity.CourseSection;
import com.example.student_management.repository.CourseSectionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("sections")
@PageTitle("Danh sách Lớp học phần")
public class CourseSectionListView extends VerticalLayout {

    public CourseSectionListView(CourseSectionRepository csRepo) {
        setSpacing(true);
        setPadding(true);

        add(new H2("🏫 Danh sách các Lớp học phần đang mở"));

        Grid<CourseSection> grid = new Grid<>(CourseSection.class, false);

        // Thêm các cột thông tin
        grid.addColumn(cs -> cs.getSubject() != null ? cs.getSubject().getSubjectName() : "N/A")
                .setHeader("Môn học").setSortable(true);

        grid.addColumn(cs -> cs.getTeacher() != null ? cs.getTeacher().getFullName() : "Chưa phân công")
                .setHeader("Giảng viên").setSortable(true);

        grid.addColumn(CourseSection::getSemester).setHeader("Học kỳ");

        // Cột chức năng để click vào xem chi tiết sinh viên
        grid.addComponentColumn(cs -> {
            Button viewDetailBtn = new Button("Xem danh sách lớp");
            viewDetailBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            viewDetailBtn.addClickListener(e ->
                    UI.getCurrent().navigate(com.example.student_management.ui.CourseSectionDetailView.class, cs.getId())
            );
            return viewDetailBtn;
        }).setHeader("Hành động");

        // Sự kiện click trực tiếp vào dòng
        grid.addItemClickListener(event ->
                UI.getCurrent().navigate(com.example.student_management.ui.CourseSectionDetailView.class, event.getItem().getId())
        );

        grid.setItems(csRepo.findAll());
        grid.getStyle().set("cursor", "pointer");

        add(grid);

        Button backBtn = new Button("⬅ Quay lại trang chủ", e -> UI.getCurrent().navigate(""));
        add(backBtn);
    }
}
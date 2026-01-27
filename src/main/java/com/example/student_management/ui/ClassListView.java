package com.example.student_management.ui;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.service.ClassService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("classes")
@PageTitle("Danh sách Lớp học")
public class ClassListView extends VerticalLayout {

    public ClassListView(ClassService classService) {
        setSpacing(true);
        setPadding(true);

        // Nút quay lại Dashboard
        Button backBtn = new Button("⬅ Quay lại Dashboard",
                e -> UI.getCurrent().navigate(MainView.class));
        add(backBtn);

        add(new H2("Danh sách Lớp học theo Ngành"));

        // Tạo bảng hiển thị ClassEntity
        Grid<ClassEntity> grid = new Grid<>(ClassEntity.class, false);

        grid.addColumn(ClassEntity::getClassName)
                .setHeader("Tên lớp học")
                .setAutoWidth(true)
                .setSortable(true);

        grid.addColumn(c -> c.getMajor() != null ? c.getMajor().getMajorName() : "N/A")
                .setHeader("Chuyên ngành")
                .setAutoWidth(true);

        // Đổ dữ liệu từ service vào grid
        grid.setItems(classService.findAll());
        grid.setAllRowsVisible(true);

        add(grid);

        grid.addItemClickListener(event -> {
            ClassEntity selectedClass = event.getItem();
            UI.getCurrent().navigate(com.example.student_management.ui.ClassDetailView.class, selectedClass.getId());
        });
    }
}
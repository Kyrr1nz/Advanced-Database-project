package com.example.student_management.ui;

import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("list")
public class StudentListView extends VerticalLayout {

    public StudentListView(StudentService service) {
        add(new Button("⬅ Quay lại Dashboard", e -> UI.getCurrent().navigate(MainView.class)));
        add(new H2("Danh sách Sinh viên theo Ngành & Lớp"));

        Grid<Student> grid = new Grid<>(Student.class, false);
        grid.addColumn(Student::getFullName).setHeader("Họ Tên").setSortable(true);

        // Hiển thị Lớp và Ngành (Dữ liệu từ Class và Major entity)
        grid.addColumn(s -> s.getClazz() != null ? s.getClazz().getClassName() : "N/A").setHeader("Lớp học");
        grid.addColumn(s -> (s.getClazz() != null && s.getClazz().getMajor() != null)
                ? s.getClazz().getMajor().getMajorName() : "N/A").setHeader("Chuyên ngành");

        grid.setItems(service.findAllForList());

        // Chuyển sang Layer 2 khi click vào dòng
        grid.addItemClickListener(event -> {
            UI.getCurrent().navigate(StudentProfileView.class, event.getItem().getId());
        });

        add(grid);
        setSizeFull();
    }
}
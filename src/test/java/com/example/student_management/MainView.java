package com.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
@PageTitle("Quản lý sinh viên")
public class MainView extends VerticalLayout {

    // ===== DATA =====
    private final List<Student> students = new ArrayList<>();
    private final Grid<Student> grid = new Grid<>(Student.class);

    public MainView() {
        setPadding(true);
        setSpacing(true);

        add(new H1("Hệ thống quản lý sinh viên"));

        // ===== FORM =====
        TextField id = new TextField("Mã sinh viên");
        TextField name = new TextField("Họ tên");
        TextField clazz = new TextField("Lớp");
        TextField email = new TextField("Email");

        Button addBtn = new Button("Thêm", e -> {
            if (id.isEmpty() || name.isEmpty()) {
                Notification.show("Mã SV và Họ tên không được trống");
                return;
            }

            students.add(new Student(
                    id.getValue(),
                    name.getValue(),
                    clazz.getValue(),
                    email.getValue()
            ));

            grid.setItems(students);
            clear(id, name, clazz, email);
            Notification.show("Đã thêm sinh viên");
        });

        HorizontalLayout form = new HorizontalLayout(
                id, name, clazz, email, addBtn
        );

        // ===== GRID =====
        grid.setColumns("id", "name", "clazz", "email");
        grid.setSizeFull();

        // ===== DELETE BUTTON =====
        Button deleteBtn = new Button("Xoá sinh viên đã chọn", e -> {
            Student selected = grid.asSingleSelect().getValue();
            if (selected != null) {
                students.remove(selected);
                grid.setItems(students);
                Notification.show("Đã xoá");
            } else {
                Notification.show("Chọn sinh viên cần xoá");
            }
        });

        add(form, grid, deleteBtn);
    }

    // ===== UTILS =====
    private void clear(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }

    // ===== MODEL (INNER CLASS) =====
    public static class Student {
        private String id;
        private String name;
        private String clazz;
        private String email;

        public Student(String id, String name, String clazz, String email) {
            this.id = id;
            this.name = name;
            this.clazz = clazz;
            this.email = email;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getClazz() { return clazz; }
        public String getEmail() { return email; }
    }
}

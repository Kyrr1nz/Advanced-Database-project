package com.example.student_management.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.student_management.entity.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    // ===== Input fields =====
    private TextField nameField  = new TextField("Họ tên");
    private TextField emailField = new TextField("Email");
    private DatePicker dobField  = new DatePicker("Ngày sinh");

    private ComboBox<String> genderField = new ComboBox<>("Giới tính");
    private TextField phoneField  = new TextField("Số điện thoại");

    // ===== Grid =====
    private Grid<Student> grid = new Grid<>(Student.class, false);

    // ===== In-memory list =====
    private List<Student> students = new ArrayList<>();

    public MainView() {

        setSizeFull();
        setPadding(true);

        // ===== Gender =====
        genderField.setItems("Nam", "Nữ");
        genderField.setPlaceholder("Chọn giới tính");

        // ===== Form layout (ngang, đẹp) =====
        FormLayout formLayout = new FormLayout(
                nameField,
                emailField,
                dobField,
                genderField,
                phoneField
        );

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        // ===== Buttons =====
        Button addButton = new Button("Thêm sinh viên");
        Button deleteButton = new Button("Xóa sinh viên đã chọn");

        HorizontalLayout buttonLayout =
                new HorizontalLayout(addButton, deleteButton);

        // ===== Grid columns =====
        grid.addColumn(Student::getId).setHeader("Mã SV");
        grid.addColumn(Student::getFullName).setHeader("Họ tên");
        grid.addColumn(Student::getEmail).setHeader("Email");
        grid.addColumn(Student::getDob).setHeader("Ngày sinh");
        grid.addColumn(Student::getGender).setHeader("Giới tính");
        grid.addColumn(Student::getPhoneNumber).setHeader("SĐT");

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();

        // ===== Add student =====
        addButton.addClickListener(e -> {
            Student student = new Student();
            student.setFullName(nameField.getValue());
            student.setEmail(emailField.getValue());
            student.setDob(dobField.getValue());
            student.setGender(genderField.getValue());
            student.setPhoneNumber(phoneField.getValue());

            students.add(student);
            grid.setItems(students);

            clearForm();
            Notification.show("Đã thêm sinh viên (UI only)");
        });

        // ===== Delete student =====
        deleteButton.addClickListener(e -> {
            Student selected = grid.asSingleSelect().getValue();
            if (selected != null) {
                students.remove(selected);
                grid.setItems(students);
                Notification.show("Đã xóa sinh viên");
            } else {
                Notification.show("Vui lòng chọn sinh viên cần xóa");
            }
        });

        // ===== Main layout =====
        add(
                formLayout,
                buttonLayout,
                grid
        );
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        dobField.clear();
        genderField.clear();
        phoneField.clear();
    }
}


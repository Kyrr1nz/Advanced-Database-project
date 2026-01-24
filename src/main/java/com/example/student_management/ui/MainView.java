package com.example.student_management.ui;

import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
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

    // ===== Service =====
    private final StudentService studentService;

    // ===== Input fields =====
    private TextField nameField  = new TextField("Họ tên");
    private TextField emailField = new TextField("Email");
    private DatePicker dobField  = new DatePicker("Ngày sinh");
    private ComboBox<String> genderField = new ComboBox<>("Giới tính");
    private TextField phoneField  = new TextField("Số điện thoại");

    // ===== Grid =====
    private Grid<Student> grid = new Grid<>(Student.class, false);

    public MainView(StudentService studentService) {
        this.studentService = studentService;

        setSizeFull();
        setPadding(true);

        configureForm();
        configureGrid();

        Button addButton = new Button("Thêm sinh viên", e -> addStudent());
        Button deleteButton = new Button("Xóa sinh viên", e -> deleteStudent());

        HorizontalLayout buttonLayout =
                new HorizontalLayout(addButton, deleteButton);

        add(
                createFormLayout(),
                buttonLayout,
                grid
        );

        refreshGrid();
    }

    // ================= CONFIG =================

    private void configureForm() {
        genderField.setItems("Nam", "Nữ");
        genderField.setPlaceholder("Chọn giới tính");
    }

    private FormLayout createFormLayout() {
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

        return formLayout;
    }

    private void configureGrid() {
        grid.addColumn(Student::getId).setHeader("Mã SV");
        grid.addColumn(Student::getFullName).setHeader("Họ tên");
        grid.addColumn(Student::getEmail).setHeader("Email");
        grid.addColumn(Student::getDob).setHeader("Ngày sinh");
        grid.addColumn(Student::getGender).setHeader("Giới tính");
        grid.addColumn(Student::getPhoneNumber).setHeader("SĐT");

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setSizeFull();
    }

    // ================= ACTIONS =================

    private void addStudent() {
        if (nameField.isEmpty() || emailField.isEmpty()) {
            Notification.show("Tên và email không được để trống");
            return;
        }

        Student student = new Student(
                nameField.getValue(),
                emailField.getValue(),
                dobField.getValue(),
                genderField.getValue(),
                phoneField.getValue()
        );

        studentService.save(student);
        refreshGrid();
        clearForm();

        Notification.show("Đã thêm sinh viên");
    }

    private void deleteStudent() {
        Student selected = grid.asSingleSelect().getValue();
        if (selected == null) {
            Notification.show("Vui lòng chọn sinh viên");
            return;
        }

        studentService.deleteById(selected.getId());
        refreshGrid();
        Notification.show("Đã xóa sinh viên");
    }

    private void refreshGrid() {
        grid.setItems(studentService.findAll());
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        dobField.clear();
        genderField.clear();
        phoneField.clear();
    }
}

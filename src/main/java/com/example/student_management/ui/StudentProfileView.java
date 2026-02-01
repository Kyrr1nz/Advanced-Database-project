package com.example.student_management.ui;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors; // Cần thiết để xử lý chuỗi lịch thi

@Route("profile")
public class StudentProfileView extends VerticalLayout implements HasUrlParameter<Long> {

    private final StudentService studentService;
    private final VerticalLayout container = new VerticalLayout();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public StudentProfileView(StudentService studentService) {
        this.studentService = studentService;
        Button backBtn = new Button("⬅ Quay lại danh sách", e -> UI.getCurrent().navigate(StudentListView.class));
        add(backBtn, container);
        container.setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, Long studentId) {
        refreshView(studentId);
    }

    private void refreshView(Long studentId) {
        container.removeAll();
        studentService.findById(studentId).ifPresent(student -> {
            // --- HEADER ---
            HorizontalLayout headerLayout = new HorizontalLayout();
            headerLayout.setWidthFull();
            headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
            headerLayout.setAlignItems(Alignment.CENTER);

            VerticalLayout infoSummary = new VerticalLayout();
            infoSummary.setSpacing(false);
            infoSummary.setPadding(false);

            infoSummary.add(new H2("Hồ sơ: " + student.getFullName()));
            Span idSpan = new Span("MSSV: " + (student.getMssv() != null ? student.getMssv() : "Chưa có"));
            idSpan.getStyle().set("font-weight", "bold").set("color", "#2b52b2");
            infoSummary.add(idSpan);

            infoSummary.add(new Span("Email: " + (student.getEmail() != null ? student.getEmail() : "Chưa cập nhật")));
            infoSummary.add(new Span("Số điện thoại: " + (student.getPhoneNumber() != null ? student.getPhoneNumber() : "Chưa cập nhật")));
            infoSummary.add(new Span("Giới tính: " + (student.getGender() != null ? student.getGender() : "Chưa xác định")));

            Button editBtn = new Button("✏️ Edit Information", e -> openEditDialog(student));
            editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            headerLayout.add(infoSummary, editBtn);
            container.add(headerLayout);

            // --- GRID DATA ---
            List<Enrollment> enrollments = studentService.getStudentFullProfile(studentId);
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setWidthFull();

            // Cột trái: Thời khóa biểu
            VerticalLayout leftCol = new VerticalLayout();
            leftCol.add(new H3("📅 Thời khóa biểu"));
            Grid<Enrollment> classGrid = new Grid<>();
            classGrid.addColumn(e -> e.getCourseSection().getSubject().getSubjectName()).setHeader("Môn học");
            classGrid.addColumn(e -> e.getCourseSection().getTeacher().getFullName()).setHeader("Giảng viên");
            classGrid.setItems(enrollments);
            classGrid.setAllRowsVisible(true);
            leftCol.add(classGrid);
            leftCol.setWidth("65%");

            // Cột phải: Lịch thi (Fix lại phần này cho Khang)
            VerticalLayout rightCol = new VerticalLayout();
            rightCol.getStyle().set("background-color", "#f9f9f9").set("border-radius", "8px");
            rightCol.add(new H3("📝 Lịch thi"));

            Grid<Enrollment> examGrid = new Grid<>();
            examGrid.addColumn(e -> e.getCourseSection().getSubject().getSubjectName()).setHeader("Môn");
            examGrid.addColumn(e -> {
                // Hiển thị ngày thi và phòng thi từ thực thể Exams
                return e.getCourseSection().getExams().stream()
                        .map(ex -> ex.getExamDate().format(dtf) + " (" + ex.getRoom() + ")")
                        .collect(Collectors.joining(", "));
            }).setHeader("Ngày/Phòng");

            examGrid.setItems(enrollments);
            examGrid.setAllRowsVisible(true);
            rightCol.add(examGrid);
            rightCol.setWidth("35%");

            mainLayout.add(leftCol, rightCol);
            container.add(mainLayout);
        });
    }

    private void openEditDialog(Student student) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Chỉnh sửa thông tin sinh viên");

        FormLayout formLayout = new FormLayout();

        TextField mssvField = new TextField("MSSV (Cố định)");
        mssvField.setValue(student.getMssv() != null ? student.getMssv() : "");
        mssvField.setReadOnly(true);

        TextField fullNameField = new TextField("Họ và Tên");
        fullNameField.setValue(student.getFullName());

        TextField emailField = new TextField("Email");
        emailField.setValue(student.getEmail());

        TextField phoneField = new TextField("Số điện thoại");
        phoneField.setValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");

        // FIX COMBOBOX: Hiện placeholder "Chọn..." thay vì gán sẵn "Nam"
        ComboBox<String> genderSelect = new ComboBox<>("Giới tính");
        genderSelect.setItems("Nam", "Nữ");
        genderSelect.setPlaceholder("Chọn giới tính");

        // Chỉ set giá trị nếu Database đã có dữ liệu
        if (student.getGender() != null && !student.getGender().isEmpty()) {
            genderSelect.setValue(student.getGender());
        }

        formLayout.add(mssvField, fullNameField, emailField, phoneField, genderSelect);
        dialog.add(formLayout);

        Button saveButton = new Button("Save", e -> {
            student.setFullName(fullNameField.getValue());
            student.setEmail(emailField.getValue());
            student.setPhoneNumber(phoneField.getValue());
            student.setGender(genderSelect.getValue());

            studentService.save(student); // Lưu vào Database

            Notification.show("Cập nhật thành công!");
            dialog.close();
            refreshView(student.getId());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }
}
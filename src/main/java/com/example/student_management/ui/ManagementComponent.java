package com.example.student_management.ui;

import com.example.student_management.entity.*;
import com.example.student_management.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H3;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class ManagementComponent extends VerticalLayout {

    private final StudentService studentService;
    private final ClassService classService;
    private final MajorService majorService;
    private final CourseSectionService courseSectionService;
    private final EnrollmentService enrollmentService;

    public ManagementComponent(StudentService studentService,
                               ClassService classService,
                               MajorService majorService,
                               CourseSectionService courseSectionService,
                               EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.classService = classService;
        this.majorService = majorService;
        this.courseSectionService = courseSectionService;
        this.enrollmentService = enrollmentService;

        Button addBtn = new Button("➕ Thêm mới dữ liệu");
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        addBtn.setWidthFull();
        addBtn.addClickListener(e -> openManagementDialog());

        add(new H3("Quản trị hệ thống"), addBtn);
        setPadding(true);
        setSpacing(true);
    }

    public void openEditDialog(Object entity) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Chỉnh sửa thông tin");
        VerticalLayout container = new VerticalLayout();

        if (entity instanceof Student s) {
            renderFullStudentForm(container, dialog, s);
        } else if (entity instanceof ClassEntity c) {
            renderFullClassForm(container, dialog, c);
        }

        dialog.add(container);
        dialog.open();
    }

    private void openManagementDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Quản lý dữ liệu & Học phần");
        dialog.setWidth("600px");

        Select<String> actionSelect = new Select<>();
        actionSelect.setLabel("Chọn tác vụ quản trị");
        actionSelect.setItems("Thêm Sinh viên", "Thêm Lớp học", "Đăng ký học phần");
        actionSelect.setWidthFull();

        VerticalLayout formContainer = new VerticalLayout();
        formContainer.setPadding(false);

        actionSelect.addValueChangeListener(event -> {
            formContainer.removeAll();
            String selected = event.getValue();
            if ("Thêm Sinh viên".equals(selected)) renderFullStudentForm(formContainer, dialog, null);
            else if ("Thêm Lớp học".equals(selected)) renderFullClassForm(formContainer, dialog, null);
            else if ("Đăng ký học phần".equals(selected)) renderFullEnrollmentForm(formContainer, dialog);
        });

        dialog.add(actionSelect, formContainer);
        dialog.open();
    }

    private void renderFullStudentForm(VerticalLayout container, Dialog dialog, Student existingStudent) {
        FormLayout form = new FormLayout();

        TextField mssvField = new TextField("MSSV");
        mssvField.setReadOnly(true);
        mssvField.setValue(existingStudent != null ? existingStudent.getMssv() : "Hệ thống tự cấp...");

        TextField name = new TextField("Họ và tên");
        name.setRequired(true);
        DatePicker dob = new DatePicker("Ngày sinh");
        TextField email = new TextField("Email");

        ComboBox<ClassEntity> classBox = new ComboBox<>("Lớp sinh hoạt");
        classBox.setItems(classService.findAll());
        classBox.setItemLabelGenerator(ClassEntity::getClassName);

        if (existingStudent != null) {
            name.setValue(existingStudent.getFullName());
            if (existingStudent.getDob() != null) dob.setValue(existingStudent.getDob());
            email.setValue(existingStudent.getEmail() != null ? existingStudent.getEmail() : "");
            classBox.setValue(existingStudent.getClazz());
        }

        Button save = new Button(existingStudent == null ? "Lưu mới" : "Cập nhật", e -> {
            if (name.isEmpty()) {
                Notification.show("Vui lòng nhập tên!");
                return;
            }

            Student s = (existingStudent != null) ? existingStudent : new Student();
            s.setFullName(name.getValue());
            s.setDob(dob.getValue());
            s.setEmail(email.getValue());
            s.setClazz(classBox.getValue());

            // ✅ TỰ ĐỘNG SINH MSSV KHI LƯU MỚI
            if (existingStudent == null) {
                String generatedMssv = "2026" + String.format("%04d", new Random().nextInt(10000));
                s.setMssv(generatedMssv);
            }

            studentService.save(s);
            Notification.show("Thành công! MSSV: " + s.getMssv());
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(mssvField, name, dob, email, classBox);
        container.add(form, save);
    }

    private void renderFullClassForm(VerticalLayout container, Dialog dialog, ClassEntity existingClass) {
        FormLayout form = new FormLayout();
        TextField className = new TextField("Tên lớp học");

        ComboBox<Major> majorBox = new ComboBox<>("Ngành học");
        majorBox.setItems(majorService.findAll());
        majorBox.setItemLabelGenerator(Major::getMajorName);

        if (existingClass != null) {
            className.setValue(existingClass.getClassName());
            majorBox.setValue(existingClass.getMajor());
        }

        Button save = new Button("Lưu", e -> {
            ClassEntity c = (existingClass != null) ? existingClass : new ClassEntity();
            c.setClassName(className.getValue());
            c.setMajor(majorBox.getValue());

            classService.save(c);
            Notification.show("Đã lưu lớp học!");
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        form.add(className, majorBox);
        container.add(form, save);
    }

    private void renderFullEnrollmentForm(VerticalLayout container, Dialog dialog) {
        FormLayout form = new FormLayout();

        ComboBox<Student> studentBox = new ComboBox<>("Chọn Sinh viên");
        studentBox.setItems(studentService.findAll());
        studentBox.setItemLabelGenerator(s -> s.getFullName() + " (" + s.getMssv() + ")");

        DatePicker enrollDate = new DatePicker("Ngày đăng ký");
        enrollDate.setValue(LocalDate.now());

        ComboBox<CourseSection> sectionBox = new ComboBox<>("Lớp học phần");
        sectionBox.setItemLabelGenerator(section ->
                (section.getSubject() != null ? section.getSubject().getSubjectName() : "N/A") + " (ID: " + section.getCourse_section_id() + ")");

        Runnable refreshSections = () -> {
            LocalDate selectedDate = enrollDate.getValue();
            if (selectedDate != null) {
                sectionBox.setItems(courseSectionService.findSectionsStartingAfter(selectedDate));
            }
        };

        enrollDate.addValueChangeListener(e -> refreshSections.run());
        refreshSections.run();

        Button save = new Button("Xác nhận Đăng ký", e -> {
            Student s = studentBox.getValue();
            CourseSection cs = sectionBox.getValue();

            if (s != null && cs != null) {
                Enrollment en = new Enrollment();
                en.setStudent(s);
                en.setCourseSection(cs);
                en.setEnrollmentDate(enrollDate.getValue());

                enrollmentService.save(en);
                Notification.show("Đăng ký thành công cho: " + s.getFullName());
                dialog.close();
            } else {
                Notification.show("Vui lòng điền đủ thông tin!");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        form.add(studentBox, enrollDate, sectionBox);
        container.add(form, save);
    }
}
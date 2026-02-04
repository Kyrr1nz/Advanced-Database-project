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

public class ManagementComponent extends VerticalLayout {

    private final StudentService studentService;
    private final ClassService classService;
    private final MajorService majorService;
    private final CourseSectionService courseSectionService;

    public ManagementComponent(StudentService studentService,
                               ClassService classService,
                               MajorService majorService,
                               CourseSectionService courseSectionService) {
        this.studentService = studentService;
        this.classService = classService;
        this.majorService = majorService;
        this.courseSectionService = courseSectionService;

        Button addBtn = new Button("➕ Thêm mới dữ liệu");
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        addBtn.setWidthFull();
        addBtn.addClickListener(e -> openManagementDialog());

        add(new H3("Quản trị hệ thống"), addBtn);
        setPadding(true);
        setSpacing(true);
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
            if ("Thêm Sinh viên".equals(selected)) renderFullStudentForm(formContainer, dialog);
            else if ("Thêm Lớp học".equals(selected)) renderFullClassForm(formContainer, dialog);
            else if ("Đăng ký học phần".equals(selected)) renderFullEnrollmentForm(formContainer, dialog);
        });

        dialog.add(actionSelect, formContainer);
        dialog.open();
    }

    private void renderFullStudentForm(VerticalLayout container, Dialog dialog) {
        FormLayout form = new FormLayout();
        TextField mssv = new TextField("MSSV (Tự động)");
        mssv.setValue("Hệ thống tự cấp...");
        mssv.setReadOnly(true);

        TextField name = new TextField("Họ và tên");
        name.setRequired(true);
        DatePicker dob = new DatePicker("Ngày sinh");
        TextField email = new TextField("Email");

        ComboBox<ClassEntity> classBox = new ComboBox<>("Lớp sinh hoạt");
        classBox.setItems(classService.findAll());
        classBox.setItemLabelGenerator(ClassEntity::getClassName);

        Button save = new Button("Lưu Sinh viên", e -> {
            Notification.show("Đã thêm sinh viên mới!");
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        form.add(mssv, name, dob, email, classBox);
        container.add(form, save);
    }

    private void renderFullClassForm(VerticalLayout container, Dialog dialog) {
        FormLayout form = new FormLayout();
        TextField classId = new TextField("Mã lớp học");
        TextField className = new TextField("Tên lớp học");

        ComboBox<Major> majorBox = new ComboBox<>("Ngành học");
        majorBox.setItems(majorService.findAll());
        majorBox.setItemLabelGenerator(Major::getMajorName);

        Button save = new Button("Tạo Lớp học", e -> {
            Notification.show("Đã tạo lớp học thành công!");
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        form.add(classId, className, majorBox);
        container.add(form, save);
    }

    // --- FORM 3: ĐĂNG KÝ HỌC PHẦN (LỌC THEO NGÀY ĐĂNG KÝ) ---
    private void renderFullEnrollmentForm(VerticalLayout container, Dialog dialog) {
        FormLayout form = new FormLayout();

        // 1. Chọn Sinh viên
        ComboBox<Student> studentBox = new ComboBox<>("Chọn Sinh viên");
        studentBox.setItems(studentService.findAll());
        studentBox.setItemLabelGenerator(Student::getFullName);

        // 2. Chọn Ngày đăng ký
        DatePicker enrollDate = new DatePicker("Ngày đăng ký");
        enrollDate.setValue(LocalDate.now());

        // 3. Chọn Lớp học phần
        ComboBox<CourseSection> sectionBox = new ComboBox<>("Lớp học phần");
        sectionBox.setPlaceholder("Chọn ngày để lọc lớp...");

        // Hiển thị: Tên môn (lấy từ Subject) + Mã lớp
        sectionBox.setItemLabelGenerator(section -> {
            String subjectTitle = (section.getSubject() != null) ?
                    section.getSubject().getSubjectName() : "Chưa có môn";
            return subjectTitle + " (Mã: " + section.getCourse_section_id() + ")";
        });

        // --- LOGIC LỌC ĐỘNG THEO NGÀY ---
        Runnable refreshSections = () -> {
            LocalDate selectedDate = enrollDate.getValue();
            if (selectedDate != null) {
                // Gọi hàm lọc lớp có startDate > selectedDate từ Service
                List<CourseSection> available = courseSectionService.findSectionsStartingAfter(selectedDate);
                sectionBox.setItems(available);
                if (available.isEmpty()) {
                    sectionBox.setPlaceholder("Không có lớp nào khai giảng sau ngày này");
                }
            }
        };

        // Lắng nghe thay đổi ngày để nạp lại danh sách lớp
        enrollDate.addValueChangeListener(e -> refreshSections.run());

        // Chạy lọc lần đầu với ngày hôm nay
        refreshSections.run();

        Button save = new Button("Xác nhận Đăng ký", e -> {
            if (studentBox.getValue() != null && sectionBox.getValue() != null) {
                Notification.show("Đăng ký thành công cho SV: " + studentBox.getValue().getFullName());
                dialog.close();
            } else {
                Notification.show("Vui lòng chọn đầy đủ thông tin!");
            }
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        form.add(studentBox, enrollDate, sectionBox);
        container.add(form, save);
    }
}
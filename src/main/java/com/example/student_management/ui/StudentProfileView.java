package com.example.student_management.ui;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.entity.Exam; // Thêm import này
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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.Comparator; // Thêm import này
import java.util.List;
import java.util.stream.Collectors;

@Route("profile")
@PageTitle("Hồ sơ Sinh viên")
public class StudentProfileView extends VerticalLayout implements HasUrlParameter<Long> {

    private final StudentService studentService;
    private final VerticalLayout container = new VerticalLayout();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public StudentProfileView(StudentService studentService) {
        this.studentService = studentService;
        setSpacing(true);
        setPadding(true);
        add(container);
        container.setSizeFull();
        container.setPadding(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Long studentId) {
        refreshView(studentId);
    }

    private void refreshView(Long studentId) {
        container.removeAll();
        studentService.findById(studentId).ifPresent(student -> {

            // 1. THANH ĐIỀU HƯỚNG
            HorizontalLayout navRow = new HorizontalLayout();
            navRow.setWidthFull();
            navRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

            Button returnBtn = new Button("Quay lại", new Icon(VaadinIcon.ARROW_BACKWARD));
            returnBtn.addClickListener(e -> UI.getCurrent().getPage().executeJs("window.history.back();"));

            Button homeBtn = new Button("Trang chủ", new Icon(VaadinIcon.HOME));
            homeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            homeBtn.addClickListener(e -> UI.getCurrent().navigate(""));

            navRow.add(returnBtn, homeBtn);
            container.add(navRow);

            // 2. TIÊU ĐỀ HỒ SƠ
            H2 profileTitle = new H2("Hồ sơ: " + student.getFullName());
            container.add(profileTitle);

            // --- THÔNG TIN TỔNG QUAN ---
            HorizontalLayout infoSummary = new HorizontalLayout();
            infoSummary.setWidthFull();
            infoSummary.setAlignItems(Alignment.END);

            VerticalLayout textInfo = new VerticalLayout();
            textInfo.setSpacing(false);
            textInfo.setPadding(false);
            textInfo.add(new Span("MSSV: " + student.getMssv()));
            textInfo.add(new Span("Email: " + student.getEmail()));
            textInfo.add(new Span("Giới tính: " + student.getGender()));

            Button editBtn = new Button("✏️ Edit Information", e -> openEditDialog(student));
            editBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

            infoSummary.add(textInfo, editBtn);
            container.add(infoSummary);

            // --- GRID DATA ---
            List<Enrollment> enrollments = studentService.getStudentFullProfile(studentId);
            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setWidthFull();

            // CỘT TRÁI: THỜI KHÓA BIỂU (Đã thêm Start/End Date)
            VerticalLayout leftCol = new VerticalLayout();
            leftCol.add(new H3("📅 Thời khóa biểu"));
            Grid<Enrollment> classGrid = new Grid<>();
            classGrid.addColumn(e -> e.getCourseSection().getSubject().getSubjectName()).setHeader("Môn học");
            classGrid.addColumn(e -> e.getCourseSection().getTeacher().getFullName()).setHeader("Giảng viên");

            // THÊM CỘT START DATE -> END DATE Ở ĐÂY
            classGrid.addColumn(e -> {
                String start = e.getCourseSection().getStartDate().format(dtf);
                String end = e.getCourseSection().getEndDate().format(dtf);
                return start + " ➔ " + end;
            }).setHeader("Thời gian học").setAutoWidth(true);

            classGrid.setItems(enrollments);
            classGrid.setAllRowsVisible(true);
            leftCol.add(classGrid);
            leftCol.setWidth("60%");

            // CỘT PHẢI: LỊCH THI (Đã sắp xếp thứ tự)
            VerticalLayout rightCol = new VerticalLayout();
            rightCol.getStyle().set("background-color", "#f9f9f9").set("border-radius", "8px");
            rightCol.add(new H3("📝 Lịch thi"));

            Grid<Exam> examGrid = new Grid<>();
            examGrid.addColumn(ex -> ex.getCourseSection().getSubject().getSubjectName()).setHeader("Môn");
            examGrid.addColumn(ex -> ex.getExamDate().format(dtf) + " (" + ex.getRoom() + ")").setHeader("Ngày/Phòng");

            // LOGIC SẮP XẾP LỊCH THI THEO THỨ TỰ THỜI GIAN
            List<Exam> sortedExams = enrollments.stream()
                    .flatMap(en -> en.getCourseSection().getExams().stream())
                    .sorted(Comparator.comparing(Exam::getExamDate)) // Sắp xếp tăng dần
                    .collect(Collectors.toList());

            examGrid.setItems(sortedExams);
            examGrid.setAllRowsVisible(true);
            rightCol.add(examGrid);
            rightCol.setWidth("40%");

            mainLayout.add(leftCol, rightCol);
            container.add(mainLayout);
        });
    }

    private void openEditDialog(Student student) {
        // ... (Giữ nguyên logic Edit Dialog của bạn)
    }

    private String getShortName(String majorName) {
        StringBuilder sb = new StringBuilder();
        for (String word : majorName.split(" ")) {
            if (!word.isEmpty()) sb.append(word.charAt(0));
        }
        return sb.toString().toUpperCase();
    }
}
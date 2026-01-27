package com.example.student_management.ui;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.entity.CourseSection;
import com.example.student_management.service.StudentService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("profile")
public class StudentProfileView extends VerticalLayout implements HasUrlParameter<Long> {

    private final StudentService studentService;
    private final VerticalLayout container = new VerticalLayout();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public StudentProfileView(StudentService studentService) {
        this.studentService = studentService;

        Button backBtn = new Button("⬅ Quay lại danh sách", e -> UI.getCurrent().navigate(com.example.student_management.ui.StudentListView.class));
        backBtn.getStyle().set("margin-bottom", "10px");

        add(backBtn, container);
        container.setSizeFull();
    }

    @Override
    public void setParameter(BeforeEvent event, Long studentId) {
        container.removeAll();
        studentService.findById(studentId).ifPresent(student -> {
            // --- HEADER: THÔNG TIN SINH VIÊN ---
            VerticalLayout header = new VerticalLayout();
            header.setSpacing(false);
            header.setPadding(false);

            header.add(new H2("Hồ sơ: " + student.getFullName()));

            // Sử dụng MSSV từ Database thay vì ID tự tăng
            Span idSpan = new Span("MSSV: " + (student.getMssv() != null ? student.getMssv() : "Chưa có"));
            idSpan.getStyle().set("font-weight", "bold");
            idSpan.getStyle().set("color", "#2b52b2");
            header.add(idSpan);

            header.add(new Span("Email: " + (student.getEmail() != null ? student.getEmail() : "Chưa cập nhật")));
            container.add(header);

            List<Enrollment> enrollments = studentService.getStudentFullProfile(studentId);

            HorizontalLayout mainLayout = new HorizontalLayout();
            mainLayout.setWidthFull();
            mainLayout.setSpacing(true);

            // --- CỘT TRÁI: THỜI KHÓA BIỂU ---
            VerticalLayout leftCol = new VerticalLayout();
            leftCol.setPadding(false);
            leftCol.add(new H3("📅 Thời khóa biểu (Click vào môn để xem danh sách lớp)"));

            Grid<Enrollment> classGrid = new Grid<>();
            classGrid.addColumn(e -> e.getCourseSection().getSubject().getSubjectName())
                    .setHeader("Môn học").setAutoWidth(true);
            classGrid.addColumn(e -> e.getCourseSection().getTeacher().getFullName())
                    .setHeader("Giảng viên").setAutoWidth(true);

            classGrid.addColumn(e -> {
                CourseSection section = e.getCourseSection();
                if (section.getStartDate() != null && section.getEndDate() != null) {
                    return section.getStartDate().format(dtf) + " ➔ " + section.getEndDate().format(dtf);
                }
                return "Chưa có ngày dự kiến";
            }).setHeader("Thời gian học").setAutoWidth(true);

            // LOGIC QUAN TRỌNG: Thêm sự kiện Click để xem chi tiết lớp học phần
            classGrid.addItemClickListener(clickEvent -> {
                Long sectionId = clickEvent.getItem().getCourseSection().getId();
                UI.getCurrent().navigate(com.example.student_management.ui.CourseSectionDetailView.class, sectionId);
            });

            classGrid.setItems(enrollments);
            classGrid.setAllRowsVisible(true);
            classGrid.getStyle().set("cursor", "pointer"); // Đổi con trỏ chuột thành hình bàn tay

            leftCol.add(classGrid);
            leftCol.setWidth("65%");

            // --- CỘT PHẢI: LỊCH THI ---
            VerticalLayout rightCol = new VerticalLayout();
            rightCol.getStyle().set("background-color", "#f9f9f9");
            rightCol.getStyle().set("border-radius", "8px");
            rightCol.setPadding(true);
            rightCol.add(new H3("📝 Lịch thi"));

            Grid<Enrollment> examGrid = new Grid<>();
            examGrid.addColumn(e -> e.getCourseSection().getSubject().getSubjectName())
                    .setHeader("Môn").setAutoWidth(true).setFlexGrow(0);

            examGrid.addColumn(e -> {
                // Lấy thông tin ngày thi và phòng thi từ thực thể Exam
                return e.getCourseSection().getExams().stream()
                        .map(ex -> ex.getExamDate().format(dtf) + " (" + ex.getRoom() + ")")
                        .collect(Collectors.joining(", "));
            }).setHeader("Ngày/Phòng").setAutoWidth(true).setFlexGrow(1);

            examGrid.setItems(enrollments);
            examGrid.setAllRowsVisible(true);
            rightCol.add(examGrid);
            rightCol.setWidth("35%");

            mainLayout.add(leftCol, rightCol);
            container.add(mainLayout);
        });
    }
}
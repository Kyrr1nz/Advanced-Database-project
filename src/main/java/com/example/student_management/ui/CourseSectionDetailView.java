package com.example.student_management.ui;

import com.example.student_management.entity.Enrollment;
import com.example.student_management.entity.Student;
import com.example.student_management.repository.CourseSectionRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route("course-section-detail")
@PageTitle("Danh sách Lớp học phần")
public class CourseSectionDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final CourseSectionRepository csRepo;
    private final VerticalLayout infoContainer = new VerticalLayout();
    private final Grid<Student> studentGrid = new Grid<>(Student.class, false);

    public CourseSectionDetailView(CourseSectionRepository csRepo) {
        this.csRepo = csRepo;

        // Nút quay lại để không bị "kẹt" ở trang này
        Button backBtn = new Button("⬅ Quay lại trang chủ", e -> UI.getCurrent().navigate(""));

        add(backBtn, new H2("Chi tiết Lớp học phần"), infoContainer, studentGrid);

        // Cấu hình Grid một lần duy nhất trong Constructor
        configureGrid();
    }

    private void configureGrid() {
        studentGrid.addColumn(Student::getMssv).setHeader("MSSV").setAutoWidth(true).setSortable(true);
        studentGrid.addColumn(Student::getFullName).setHeader("Họ Tên").setAutoWidth(true).setSortable(true);
        studentGrid.addColumn(s -> s.getClazz() != null ? s.getClazz().getClassName() : "N/A")
                .setHeader("Lớp hành chính");
        studentGrid.addColumn(s -> (s.getClazz() != null && s.getClazz().getMajor() != null)
                        ? s.getClazz().getMajor().getMajorName() : "N/A")
                .setHeader("Chuyên ngành");
    }

    @Override
    public void setParameter(BeforeEvent event, Long sectionId) {
        csRepo.findById(sectionId).ifPresent(section -> {
            infoContainer.removeAll();

            // Hiện thông tin lớp & giảng viên
            String subName = section.getSubject() != null ? section.getSubject().getSubjectName() : "N/A";
            String teaName = section.getTeacher() != null ? section.getTeacher().getFullName() : "Chưa phân công";

            infoContainer.add(
                    new Span("📖 Môn học: " + subName),
                    new Span("👨‍🏫 Giảng viên: " + teaName),
                    new Span("📅 Thời gian: " + section.getStartDate() + " ⮕ " + section.getEndDate()),
                    new Span("👥 Sĩ số: " + (section.getEnrollments() != null ? section.getEnrollments().size() : 0))
            );

            // LOGIC LẤY SINH VIÊN: Chuyển từ Enrollment sang Student
            if (section.getEnrollments() != null) {
                List<Student> students = section.getEnrollments().stream()
                        .map(Enrollment::getStudent)
                        .collect(Collectors.toList());
                studentGrid.setItems(students);
            }
        });
    }
}
package com.example.student_management.ui;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.entity.Student;
import com.example.student_management.service.ClassService;
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

@Route("class-detail")
@PageTitle("Chi tiết Lớp học")
public class ClassDetailView extends VerticalLayout implements HasUrlParameter<Long> {

    private final ClassService classService;
    private final VerticalLayout container = new VerticalLayout();

    public ClassDetailView(ClassService classService) {
        this.classService = classService;

        Button backBtn = new Button("⬅ Quay lại danh sách lớp",
                e -> UI.getCurrent().navigate(ClassListView.class));

        add(backBtn, container);
        container.setPadding(false);
    }

    @Override
    public void setParameter(BeforeEvent event, Long classId) {
        container.removeAll();

        // Giả sử classService có hàm findById trả về Optional hoặc ClassEntity
        classService.findById(classId).ifPresent(classEntity -> {

            // Tiêu đề lớp học
            H2 title = new H2("Lớp: " + classEntity.getClassName());
            Span majorInfo = new Span("Chuyên ngành: " +
                    (classEntity.getMajor() != null ? classEntity.getMajor().getMajorName() : "N/A"));
            majorInfo.getStyle().set("color", "#64748b");

            container.add(title, majorInfo);

            // Bảng danh sách sinh viên trong lớp
            Grid<Student> studentGrid = new Grid<>(Student.class, false);

            // Hiển thị Mã sinh viên và Họ tên theo yêu cầu của Khang
            studentGrid.addColumn(Student::getMssv)
                    .setHeader("MSSV")
                    .setAutoWidth(true)
                    .setSortable(true);

            studentGrid.addColumn(Student::getFullName)
                    .setHeader("Họ và Tên")
                    .setAutoWidth(true)
                    .setSortable(true);

            studentGrid.addColumn(Student::getEmail)
                    .setHeader("Email")
                    .setAutoWidth(true);

            // Lấy danh sách sinh viên từ quan hệ của ClassEntity
            // Lưu ý: Đảm bảo trong Entity ClassEntity có: List<Student> getStudents()
            studentGrid.setItems(classEntity.getStudents());
            studentGrid.setAllRowsVisible(true);

            container.add(studentGrid);
        });
    }
}
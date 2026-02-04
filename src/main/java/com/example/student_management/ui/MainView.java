package com.example.student_management.ui;

import com.example.student_management.service.StudentService;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.MajorService;
import com.example.student_management.service.CourseSectionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.jdbc.core.JdbcTemplate;

@Route("")
public class MainView extends VerticalLayout {

    public MainView(StudentService studentService,
                    ClassService classService,
                    MajorService majorService,
                    CourseSectionService courseSectionService, // Cần thêm Service này vào đây
                    JdbcTemplate jdbcTemplate) {

        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f5f7fa");

        // 1. HEADER
        Header header = new Header();
        header.setWidthFull();
        header.getStyle().set("background", "linear-gradient(to right, #2b52b2, #3a7bd5)")
                .set("padding", "20px").set("border-radius", "12px").set("color", "white");
        H1 title = new H1("🎓 Student Management System");
        title.getStyle().set("margin", "0").set("font-size", "24px");
        header.add(title);
        add(header);

        // 2. DASHBOARD OVERVIEW
        HorizontalLayout cardsLayout = new HorizontalLayout();
        cardsLayout.setWidthFull();
        cardsLayout.setSpacing(true);

        cardsLayout.add(createDashboardCol("Students", studentService.countStudents(),
                VaadinIcon.USERS, "#3b82f6", "View List", StudentListView.class));
        cardsLayout.add(createDashboardCol("Majors", majorService.countMajors(),
                VaadinIcon.ACADEMY_CAP, "#10b981", "Manage Classes", ClassListView.class));
        cardsLayout.add(createDashboardCol("Sections", courseSectionService.countSections(), // Dùng CourseSectionService
                VaadinIcon.BOOK, "#f59e0b", "Manage Sections", CourseSectionListView.class));
        add(cardsLayout);

        // 3. BOTTOM ROW: PROJECT INFO & SQL PERFORMANCE LAB
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidthFull();
        bottomLayout.setSpacing(true);
        bottomLayout.setAlignItems(Alignment.STRETCH);
        bottomLayout.getStyle().set("margin-top", "30px");

        // --- CỘT TRÁI: PROJECT INFO & MANAGEMENT (40%) ---
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setWidth("40%");
        leftColumn.setPadding(false);
        leftColumn.setSpacing(true);

        // Thẻ thông tin dự án
        VerticalLayout infoCard = new VerticalLayout();
        infoCard.getStyle().set("background-color", "white").set("border", "1px solid #e2e8f0")
                .set("border-radius", "12px").set("padding", "25px");

        H3 infoHeader = new H3("ℹ️ PROJECT INFORMATION");
        infoHeader.getStyle().set("border-bottom", "2px solid #f3f4f6").set("width", "100%").set("padding-bottom", "10px");

        Div content = new Div();
        content.add(new Paragraph("Project: Student Management System"));
        content.add(new Paragraph("Course : Advanced Database Systems"));
        content.add(new Paragraph("Group  : 7"));
        UnorderedList members = new UnorderedList(
                new ListItem("Bảo Khang"), new ListItem("Đình Quốc"),
                new ListItem("Duy Thành"), new ListItem("Đình Phước")
        );
        content.add(new Span("Members:"), members);
        infoCard.add(infoHeader, content);

        // --- MANAGEMENT COMPONENT ---
        // Khởi tạo và truyền đủ 4 Service cho Khang
        ManagementComponent managementPart = new ManagementComponent(
                studentService,
                classService,
                majorService,
                courseSectionService
        );
        managementPart.getStyle().set("background-color", "white")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "12px");

        leftColumn.add(infoCard, managementPart);

        // --- CỘT PHẢI: SQL PERFORMANCE LAB (60%) ---
        SqlPerformanceLab sqlLab = new SqlPerformanceLab(jdbcTemplate);
        sqlLab.setWidth("60%");
        sqlLab.getStyle().set("margin", "0");

        bottomLayout.add(leftColumn, sqlLab);
        add(bottomLayout);
    }

    private VerticalLayout createDashboardCol(String title, long value, VaadinIcon icon, String color, String btnText, Class<? extends com.vaadin.flow.component.Component> targetView) {
        VerticalLayout col = new VerticalLayout();
        col.setAlignItems(Alignment.CENTER);
        col.setPadding(false);
        col.getStyle().set("flex", "1");

        Div badge = new Div();
        badge.setWidthFull();
        badge.getStyle().set("padding", "25px").set("border-radius", "12px 12px 0 0")
                .set("background-color", "white").set("border-top", "5px solid " + color)
                .set("text-align", "center").set("box-shadow", "0 4px 6px rgba(0,0,0,0.05)");

        Icon vIcon = icon.create();
        vIcon.getStyle().set("color", color).set("font-size", "2rem");
        H1 count = new H1(String.valueOf(value));
        count.getStyle().set("margin", "10px 0");
        Span label = new Span(title);
        label.getStyle().set("color", "#64748b").set("font-weight", "bold");
        badge.add(vIcon, count, label);

        Button actionBtn = new Button(btnText, e -> UI.getCurrent().navigate(targetView));
        actionBtn.setWidthFull();
        actionBtn.getStyle()
                .set("background-color", color).set("color", "white")
                .set("border-radius", "0 0 12px 12px").set("height", "50px")
                .set("font-weight", "bold").set("cursor", "pointer");

        col.add(badge, actionBtn);
        return col;
    }
}
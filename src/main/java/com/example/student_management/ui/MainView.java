package com.example.student_management.ui;

import com.example.student_management.service.*;
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
                    CourseSectionService courseSectionService,
                    JdbcTemplate jdbcTemplate) {

        // Cấu hình nền và khoảng cách tổng thể
        setSpacing(false);
        setPadding(false);
        setAlignItems(Alignment.STRETCH);
        getStyle().set("background-color", "#f0f2f5") // Màu nền trung tính
                .set("font-family", "'Inter', sans-serif");

        // 1. TOP NAVIGATION BAR (Thanh điều hướng gọn gàng)
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.getStyle()
                .set("background-color", "#ffffff")
                .set("padding", "10px 40px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.05)")
                .set("justify-content", "space-between")
                .set("align-items", "center");

        H2 logo = new H2("🎓 SMS Dashboard");
        logo.getStyle().set("margin", "0").set("color", "#1a73e8").set("font-size", "20px");

        Span userRole = new Span("Admin Group 7");
        userRole.getStyle().set("color", "#5f6368").set("font-weight", "500");

        topBar.add(logo, userRole);
        add(topBar);

        // 2. MAIN CONTENT AREA
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setMaxWidth("1400px");
        content.getStyle().set("margin", "0 auto");

        // 3. STATISTIC CARDS (Thẻ thống kê hiện đại)
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.getStyle().set("margin-top", "20px");

        statsLayout.add(createStatCard("Students", studentService.countStudents(), VaadinIcon.USERS, "#1a73e8", StudentListView.class));
        statsLayout.add(createStatCard("Majors", majorService.countMajors(), VaadinIcon.ACADEMY_CAP, "#34a853", ClassListView.class));
        statsLayout.add(createStatCard("Sections", courseSectionService.countSections(), VaadinIcon.NOTEBOOK, "#f9ab00", CourseSectionListView.class));
        content.add(statsLayout);

        // 4. BOTTOM GRID: PROJECT & LAB
        HorizontalLayout bottomGrid = new HorizontalLayout();
        bottomGrid.setWidthFull();
        bottomGrid.setSpacing(true);
        bottomGrid.getStyle().set("margin-top", "24px");

        // --- CỘT TRÁI: PROJECT INFO ---
        VerticalLayout leftSide = new VerticalLayout();
        leftSide.setWidth("35%");
        leftSide.setPadding(false);

        Div infoCard = new Div();
        infoCard.setWidthFull();
        infoCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "24px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        H3 infoHeader = new H3("Project Information");
        infoHeader.getStyle().set("margin-top", "0").set("border-bottom", "1px solid #eee").set("padding-bottom", "10px");

        infoCard.add(infoHeader);
        infoCard.add(new Paragraph("🚀 System: Student Management"));
        infoCard.add(new Paragraph("📚 Course: Advanced Database Systems"));
        infoCard.add(new Paragraph("👥 Group: 7"));

        UnorderedList members = new UnorderedList(
                new ListItem("Bảo Khang"), new ListItem("Đình Quốc"),
                new ListItem("Duy Thành"), new ListItem("Đình Phước")
        );
        infoCard.add(new Span("Core Members:"), members);

        // Quick Management Integration
        ManagementComponent managePart = new ManagementComponent(studentService, classService, majorService, courseSectionService);
        managePart.getStyle().set("margin-top", "20px").set("border-radius", "12px");

        leftSide.add(infoCard, managePart);

        // --- CỘT PHẢI: SQL LAB ---
        SqlPerformanceLab sqlLab = new SqlPerformanceLab(jdbcTemplate);
        sqlLab.setWidth("65%");
        sqlLab.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        bottomGrid.add(leftSide, sqlLab);
        content.add(bottomGrid);

        add(content);
    }

    private VerticalLayout createStatCard(String title, long value, VaadinIcon icon, String color, Class<? extends com.vaadin.flow.component.Component> targetView) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 1px 2px rgba(0,0,0,0.06)")
                .set("border-left", "5px solid " + color)
                .set("cursor", "pointer")
                .set("transition", "transform 0.2s");

        card.getElement().executeJs("this.addEventListener('mouseenter', () => { this.style.transform = 'translateY(-5px)'; });");
        card.getElement().executeJs("this.addEventListener('mouseleave', () => { this.style.transform = 'translateY(0)'; });");
        card.addClickListener(e -> UI.getCurrent().navigate(targetView));

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);
        Icon i = icon.create();
        i.getStyle().set("color", color);
        Span s = new Span(title);
        s.getStyle().set("color", "#5f6368").set("font-weight", "600");
        header.add(i, s);

        H1 val = new H1(String.valueOf(value));
        val.getStyle().set("margin", "10px 0 0 0").set("font-size", "32px");

        card.add(header, val);
        return card;
    }
}
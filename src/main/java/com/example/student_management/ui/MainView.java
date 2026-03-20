
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
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    public MainView(StudentService studentService,
                    ClassService classService,
                    MajorService majorService,
                    CourseSectionService courseSectionService) {

        // Cấu hình nền và khoảng cách tổng thể
        setSpacing(false);
        setPadding(false);
        setAlignItems(Alignment.STRETCH);
        getStyle().set("background-color", "#f0f2f5")
                .set("font-family", "'Inter', sans-serif");

        // 1. TOP NAVIGATION BAR
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

        // 3. STATISTIC CARDS
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.getStyle().set("margin-top", "20px");

        statsLayout.add(createStatCard("Students", studentService.countStudents(), VaadinIcon.USERS, "#1a73e8", StudentListView.class));
        statsLayout.add(createStatCard("Majors", majorService.countMajors(), VaadinIcon.ACADEMY_CAP, "#34a853", ClassListView.class));
        statsLayout.add(createStatCard("Sections", courseSectionService.countSections(), VaadinIcon.NOTEBOOK, "#f9ab00", CourseSectionListView.class));
        content.add(statsLayout);

        // 4. BOTTOM AREA: PROJECT INFO & MANAGEMENT

        VerticalLayout mainBottomLayout = new VerticalLayout();
        mainBottomLayout.setWidthFull();
        mainBottomLayout.setPadding(false);
        mainBottomLayout.setSpacing(true);
        mainBottomLayout.getStyle().set("margin-top", "24px");

        // --- PROJECT INFO CARD ---
        Div infoCard = new Div();
        infoCard.setWidthFull();
        infoCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "24px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        H3 infoHeader = new H3("Project Information");
        infoHeader.getStyle().set("margin-top", "0").set("border-bottom", "1px solid #eee").set("padding-bottom", "10px");

        // --- MANAGEMENT COMPONENT ---
        ManagementComponent managePart = new ManagementComponent(studentService, classService, majorService, courseSectionService);
        managePart.getStyle().set("margin-top", "20px").set("border-radius", "12px");
        managePart.setWidthFull();

        mainBottomLayout.add(infoCard, managePart);
        content.add(mainBottomLayout);

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


        card.add(header);
        card.setHeight("80px");
        card.setJustifyContentMode(JustifyContentMode.CENTER);
        return card;
    }
}
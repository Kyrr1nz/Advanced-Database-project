
package com.example.student_management.ui;

import com.example.student_management.entity.ClassEntity;
import com.example.student_management.entity.Student;
import com.example.student_management.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {
    // Khai báo ở đây để dùng được trong toàn bộ Class
    private ManagementComponent managePart;
    private ComboBox<Object> globalSearch;

    public MainView(StudentService studentService,
                    ClassService classService,
                    MajorService majorService,
                    CourseSectionService courseSectionService) {
        this.managePart = new ManagementComponent(studentService, classService, majorService, courseSectionService);

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

    //Find student/class global
        ComboBox<Object> globalSearch = new ComboBox<>("");
        globalSearch.setPlaceholder("Tìm sinh viên, lớp hoặc môn học...");
        globalSearch.setWidth("400px");
        globalSearch.setClearButtonVisible(true);
        globalSearch.setPrefixComponent(VaadinIcon.SEARCH.create());
        globalSearch.setItems(
                query -> {
                    // Lấy thông số phân trang (bắt buộc phải gọi để tránh lỗi trên)
                    int offset = query.getOffset();
                    int limit = query.getLimit();

                    String filter = query.getFilter().orElse("").trim();
                    if (filter.isEmpty()) {
                        return Stream.empty();
                    }

                    List<Object> results = new ArrayList<>();
                    results.addAll(studentService.search(filter));
                    results.addAll(classService.search(filter));

                    // Cắt bớt danh sách dựa trên yêu cầu của Vaadin (để thỏa mãn 'contract')
                    return results.stream().skip(offset).limit(limit);
                }
        );
        globalSearch.setItemLabelGenerator(item -> {
            if (item instanceof Student s) {
                String name = s.getFullName() != null ? s.getFullName() : "Không tên";
                String code = s.getMssv() != null ? s.getMssv() : "N/A";
                return "🎓 SV: " + name + " (" + code + ")";
            } else if (item instanceof ClassEntity c) {
                return "🏫 Lớp: " + (c.getClassName() != null ? c.getClassName() : "Chưa đặt tên");
            }
            return "";
        });


        globalSearch.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                // Gọi method mở Dialog CRUD từ ManagementComponent
                managePart.openEditDialog(e.getValue());
                globalSearch.clear();
            }
        });

        topBar.add(logo, globalSearch, userRole);
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
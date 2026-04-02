package com.example.student_management.ui;

import com.example.student_management.entity.*;
import com.example.student_management.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route("")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    private final ManagementComponent managePart;
    private final ComboBox<Object> globalSearch;

    public MainView(StudentService studentService,
                    ClassService classService,
                    MajorService majorService,
                    CourseSectionService courseSectionService,
                    ExamService examService,
                    SubjectService subjectService,
                    EnrollmentService enrollmentService) { // ✅ Đã thêm EnrollmentService

        // ✅ Chỉ khởi tạo 1 lần và truyền ĐỦ 5 tham số để fix lỗi Length Mismatch
        this.managePart = new ManagementComponent(
                studentService,
                classService,
                majorService,
                courseSectionService,
                enrollmentService
        );

        // UI Config
        setSpacing(false);
        setPadding(false);
        setAlignItems(Alignment.STRETCH);
        getStyle().set("background-color", "#f0f2f5")
                .set("font-family", "'Inter', sans-serif");

        // =========================
        // 🔝 TOP BAR
        // =========================
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

        // =========================
        // 🔍 SEARCH LOGIC
        // =========================
        globalSearch = new ComboBox<>("");
        globalSearch.setPlaceholder("Tìm sinh viên, lớp hoặc môn học...");
        globalSearch.setWidth("400px");
        globalSearch.setClearButtonVisible(true);
        globalSearch.setPrefixComponent(VaadinIcon.SEARCH.create());

        globalSearch.setItems(query -> {
            String filter = query.getFilter().orElse("").trim();
            if (filter.isEmpty()) return Stream.empty();

            List<Object> results = new ArrayList<>();
            results.addAll(studentService.search(filter));
            results.addAll(classService.search(filter));
            results.addAll(subjectService.search(filter));

            return results.stream().skip(query.getOffset()).limit(query.getLimit());
        });

        globalSearch.setItemLabelGenerator(item -> {
            if (item instanceof Student s) return "🎓 SV: " + s.getFullName() + " (" + s.getMssv() + ")";
            if (item instanceof ClassEntity c) return "🏫 Lớp: " + c.getClassName();
            if (item instanceof Subject sub) return "📚 Môn: " + sub.getSubjectName();
            return "Kết quả khác";
        });

        globalSearch.addValueChangeListener(e -> {
            Object selected = e.getValue();
            if (selected != null) {
                if (selected instanceof Student s) UI.getCurrent().navigate(StudentProfileView.class, s.getId());
                else if (selected instanceof ClassEntity c) UI.getCurrent().navigate(ClassDetailView.class, c.getClass_id());
                else Notification.show("Hãy chọn chi tiết trong Profile SV.");
                globalSearch.clear();
            }
        });

        Button logoutBtn = new Button("Logout", e -> {
            UI.getCurrent().getSession().setAttribute("user", null);
            UI.getCurrent().navigate("login");
        });
        logoutBtn.getStyle().set("background", "#ea4335").set("color", "white");

        topBar.add(logo, globalSearch, userRole, logoutBtn);
        add(topBar);

        // =========================
        // 📦 CONTENT & STATS
        // =========================
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setMaxWidth("1400px");
        content.getStyle().set("margin", "0 auto");

        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.add(
                createStatCard("Students", studentService.countStudents(), VaadinIcon.USERS, "#1a73e8", StudentListView.class),
                createStatCard("Majors", majorService.countMajors(), VaadinIcon.ACADEMY_CAP, "#34a853", ClassListView.class),
                createStatCard("Sections", courseSectionService.countSections(), VaadinIcon.NOTEBOOK, "#f9ab00", CourseSectionListView.class),
                createStatCard("Exams", (long) examService.getAllExams().size(), VaadinIcon.CALENDAR, "#8e24aa", ExamListView.class)
        );
        content.add(statsLayout);

        // =========================
        // 🛠 MANAGEMENT PART
        // =========================
        Div infoCard = new Div();
        infoCard.setWidthFull();
        infoCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "24px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        this.managePart.setWidthFull();
        infoCard.add(this.managePart);
        content.add(infoCard);

        add(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (UI.getCurrent().getSession().getAttribute("user") == null) {
            event.forwardTo("login");
        }
    }

    private VerticalLayout createStatCard(String title, long value, VaadinIcon icon, String color,
                                          Class<? extends com.vaadin.flow.component.Component> targetView) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("border-left", "5px solid " + color)
                .set("cursor", "pointer");

        card.addClickListener(e -> UI.getCurrent().navigate(targetView));

        HorizontalLayout header = new HorizontalLayout(icon.create(), new Span(title + ": " + value));
        header.getComponentAt(0).getStyle().set("color", color);
        card.add(header);

        return card;
    }
}
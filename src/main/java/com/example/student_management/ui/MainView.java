package com.example.student_management.ui;

import com.example.student_management.entity.Exam;
import java.util.List;
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

    private ManagementComponent managePart;
    private ComboBox<Object> globalSearch;

    public MainView(StudentService studentService,
                    ClassService classService,
                    MajorService majorService,
                    CourseSectionService courseSectionService,
                    ExamService examService,
                    SubjectService subjectService) {

        this.managePart = new ManagementComponent(studentService, classService, majorService, courseSectionService);

        // UI config
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
        // 🔍 SEARCH (GIỮ NGUYÊN LOGIC)
        // =========================
        globalSearch = new ComboBox<>("");
        globalSearch.setPlaceholder("Tìm sinh viên, lớp hoặc môn học...");
        globalSearch.setWidth("400px");
        globalSearch.setClearButtonVisible(true);
        globalSearch.setPrefixComponent(VaadinIcon.SEARCH.create());

        globalSearch.setItems(query -> {
            int offset = query.getOffset();
            int limit = query.getLimit();

            String filter = query.getFilter().orElse("").trim();
            if (filter.isEmpty()) {
                return Stream.empty();
            }

            List<Object> results = new ArrayList<>();

            List<Student> students = studentService.search(filter);
            List<ClassEntity> classes = classService.search(filter);
            List<Subject> subjects = subjectService.search(filter);

            if (students != null) results.addAll(students);
            if (classes != null) results.addAll(classes);
            if (subjects != null) results.addAll(subjects);

            return results.stream().skip(offset).limit(limit);
        });

        globalSearch.setItemLabelGenerator(item -> {
            if (item instanceof Student s) {
                String name = s.getFullName() != null ? s.getFullName() : "Không tên";
                String code = s.getMssv() != null ? s.getMssv() : "N/A";
                return "🎓 SV: " + name + " (" + code + ")";
            } else if (item instanceof ClassEntity c) {
                return "🏫 Lớp: " + (c.getClassName() != null ? c.getClassName() : "Chưa đặt tên");
            } else if (item instanceof Subject sub) {
                return "📚 Môn: " + (sub.getSubjectName() != null ? sub.getSubjectName() : "N/A");
            }
            return "Kết quả khác";
        });

        globalSearch.addValueChangeListener(e -> {
            Object selected = e.getValue();
            if (selected != null) {
                if (selected instanceof Student s) {
                    // Dẫn đến trang Hồ sơ sinh viên (StudentProfileView)
                    UI.getCurrent().navigate(StudentProfileView.class, s.getId());

                } else if (selected instanceof ClassEntity c) {
                    // Dẫn đến trang Chi tiết lớp học (ClassDetailView)
                    UI.getCurrent().navigate(ClassDetailView.class, c.getClass_id());

                } else if (selected instanceof Subject sub) {
                    /**
                     * Lưu ý: Trang CourseSectionDetailView của Khang nhận tham số là sectionId.
                     * Vì 1 môn học (Subject) có thể có nhiều lớp học phần, nên Khang cần cân nhắc:
                     * Cách 1: Dẫn tới trang danh sách lớp học phần của môn đó (nếu có).
                     * Cách 2: Nếu Khang muốn dẫn thẳng vào chi tiết, ta cần lấy 1 Section ID mẫu.
                     */
                    Notification.show("Môn học: " + sub.getSubjectName() + " - Hãy chọn lớp học phần cụ thể trong Profile SV.");
                }

                globalSearch.clear(); // Xóa thanh tìm kiếm sau khi nhảy trang
            }
        });

        // =========================
        // 🔓 LOGOUT
        // =========================
        Button logoutBtn = new Button("Logout", e -> {
            UI.getCurrent().getSession().setAttribute("user", null);
            UI.getCurrent().navigate("login");
        });

        logoutBtn.getStyle()
                .set("background", "#ea4335")
                .set("color", "white");

        topBar.add(logo, globalSearch, userRole, logoutBtn);
        add(topBar);

        // =========================
        // 📦 CONTENT
        // =========================
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setMaxWidth("1400px");
        content.getStyle().set("margin", "0 auto");

        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.getStyle().set("margin-top", "20px");

        statsLayout.add(
                createStatCard("Students", studentService.countStudents(), VaadinIcon.USERS, "#1a73e8", StudentListView.class),
                createStatCard("Majors", majorService.countMajors(), VaadinIcon.ACADEMY_CAP, "#34a853", ClassListView.class),
                createStatCard("Sections", courseSectionService.countSections(), VaadinIcon.NOTEBOOK, "#f9ab00", CourseSectionListView.class),
                createStatCard("Exams", examService.getAllExams().size(), VaadinIcon.CALENDAR, "#8e24aa", ExamListView.class)
        );

        content.add(statsLayout);

        VerticalLayout mainBottomLayout = new VerticalLayout();
        mainBottomLayout.setWidthFull();
        mainBottomLayout.setPadding(false);
        mainBottomLayout.setSpacing(true);
        mainBottomLayout.getStyle().set("margin-top", "24px");

        Div infoCard = new Div();
        infoCard.setWidthFull();
        infoCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "24px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)");

        ManagementComponent managePart = new ManagementComponent(studentService, classService, majorService, courseSectionService);
        managePart.setWidthFull();

        mainBottomLayout.add(infoCard, managePart);
        content.add(mainBottomLayout);

        add(content);
    }

    // ✅ LOGIN GUARD CHUẨN (KHÔNG BUG REFRESH)
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Object user = UI.getCurrent().getSession().getAttribute("user");
        if (user == null) {
            event.forwardTo("login");
        }
    }

    private VerticalLayout createStatCard(String title, long value, VaadinIcon icon, String color,
                                          Class<? extends com.vaadin.flow.component.Component> targetView) {

        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 1px 2px rgba(0,0,0,0.06)")
                .set("border-left", "5px solid " + color)
                .set("cursor", "pointer");

        card.addClickListener(e -> UI.getCurrent().navigate(targetView));

        HorizontalLayout header = new HorizontalLayout();
        header.setAlignItems(Alignment.CENTER);

        Icon i = icon.create();
        i.getStyle().set("color", color);

        Span s = new Span(title);
        s.getStyle().set("color", "#5f6368").set("font-weight", "600");

        header.add(i, s);
        card.add(header);

        return card;
    }
}
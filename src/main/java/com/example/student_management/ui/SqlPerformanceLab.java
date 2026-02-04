package com.example.student_management.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class SqlPerformanceLab extends VerticalLayout {

    private final JdbcTemplate jdbcTemplate;

    public SqlPerformanceLab(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        // Cấu hình giao diện khung trắng, bo góc
        getStyle().set("background-color", "white")
                .set("border", "1px solid #e2e8f0")
                .set("border-radius", "12px")
                .set("padding", "25px");
        setWidthFull();

        H3 labHeader = new H3("🚀 SQL PERFORMANCE LAB");
        labHeader.getStyle().set("border-bottom", "2px solid #f3f4f6")
                .set("width", "100%")
                .set("padding-bottom", "10px");

        TextArea sqlInput = new TextArea();
        sqlInput.setPlaceholder("Dán câu SQL Multi-join vào đây để xem insight...");
        sqlInput.setWidthFull();
        sqlInput.setHeight("120px");

        Span statusLabel = new Span("⏱ Trạng thái: Chờ query...");
        statusLabel.getStyle().set("color", "#64748b").set("font-style", "italic");

        Grid<Map<String, Object>> dynamicGrid = new Grid<>();
        dynamicGrid.setHeight("300px"); // Tăng độ cao để dễ nhìn insight

        Button runBtn = new Button("Thực thi & Phân tích", new Icon(VaadinIcon.DATABASE));
        runBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        runBtn.addClickListener(e -> {
            String sql = sqlInput.getValue();
            if (sql == null || sql.trim().isEmpty()) return;
            executeSql(sql, dynamicGrid, statusLabel);
        });

        add(labHeader, sqlInput, runBtn, statusLabel, dynamicGrid);
    }

    private void executeSql(String sql, Grid<Map<String, Object>> grid, Span label) {
        try {
            grid.removeAllColumns();
            long start = System.currentTimeMillis(); // Bắt đầu đo hiệu năng

            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql); // Chạy query từ Java

            long end = System.currentTimeMillis();
            long duration = end - start; // Tính thời gian thực thi

            if (!data.isEmpty()) {
                // Tự động bóc tách cột (Dynamic Insight)
                data.getFirst().keySet().forEach(col -> {
                    grid.addColumn(map -> map.get(col)).setHeader(col.toUpperCase()).setAutoWidth(true);
                });
                grid.setItems(data);
                label.setText("✅ Hoàn tất: " + duration + "ms | Tìm thấy: " + data.size() + " dòng.");
                label.getStyle().set("color", "#10b981").set("font-weight", "bold");
            } else {
                label.setText("✅ Thực thi trong " + duration + "ms. Không có dữ liệu.");
            }
        } catch (Exception ex) {
            label.setText("❌ Lỗi SQL! Vui lòng kiểm tra lại cú pháp.");
            label.getStyle().set("color", "#ef4444");
        }
    }
}
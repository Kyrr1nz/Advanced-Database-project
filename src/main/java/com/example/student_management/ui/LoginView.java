package com.example.student_management.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {

        // =========================
        // 🌌 BACKGROUND
        // =========================
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("background",
                "linear-gradient(135deg, #1a73e8, #6a11cb)");

        // =========================
        // 📦 LOGIN CARD
        // =========================
        VerticalLayout card = new VerticalLayout();
        card.setWidth("350px");
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(Alignment.CENTER);

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("box-shadow", "0 8px 25px rgba(0,0,0,0.2)")
                .set("padding", "30px");

        // =========================
        // 🎓 TITLE
        // =========================
        H2 title = new H2("Welcome Back");
        title.getStyle()
                .set("margin", "0")
                .set("color", "#1a73e8");

        Span subtitle = new Span("Login to your account");
        subtitle.getStyle().set("color", "#666");

        // =========================
        // 🔑 INPUT
        // =========================
        TextField username = new TextField("Username");
        username.setWidthFull();

        PasswordField password = new PasswordField("Password");
        password.setWidthFull();

        // =========================
        // 🔘 LOGIN BUTTON
        // =========================
        Button loginBtn = new Button("Login", e -> {
            if ("hutechadmin".equals(username.getValue()) &&
                    "123@".equals(password.getValue())) {

                UI.getCurrent().getSession().setAttribute("user", username.getValue());
                UI.getCurrent().navigate("");

            } else {
                Notification.show("Sai tài khoản hoặc mật khẩu ❌");
            }
        });

        loginBtn.setWidthFull();
        loginBtn.getStyle()
                .set("background", "#1a73e8")
                .set("color", "white")
                .set("border-radius", "10px")
                .set("font-weight", "bold");

        // =========================
        // 💡 FOOTER
        // =========================
        Span footer = new Span("© 2026 Student Management System");
        footer.getStyle()
                .set("font-size", "12px")
                .set("color", "#aaa");

        // =========================
        // ADD ALL
        // =========================
        card.add(title, subtitle, username, password, loginBtn, footer);

        add(card);
    }
}
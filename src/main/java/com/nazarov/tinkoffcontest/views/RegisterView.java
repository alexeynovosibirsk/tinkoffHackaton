package com.nazarov.tinkoffcontest.views;



import com.nazarov.tinkoffcontest.entity.User;
import com.nazarov.tinkoffcontest.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Register")
public class RegisterView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    private UserService userService;

    public RegisterView(UserService userService) {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        Notification notification = new Notification();

        this.getElement().getStyle().set(
                "background",
                "url(../images/bg.jpg)" +
                        "no-repeat center center fixed"
        );

        TextField usernameField = new TextField("Username");
        usernameField.setClearButtonVisible(true);

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setClearButtonVisible(true);

        EmailField emailField = new EmailField("Email");
        emailField.setClearButtonVisible(true);

        Button button = new Button("Зарегистрироваться");
        button.addClickShortcut(Key.ENTER);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {

            User us = new User();

            if (usernameField.getValue().isEmpty() || passwordField.getValue().isEmpty() || emailField.getValue().isEmpty()) {
                notification.show("Не все поля заполнены!",
                        3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {

                us.setUsername(usernameField.getValue());
                us.setPassword(passwordField.getValue());
                us.setEmail(emailField.getValue());


                if (userService.addUser(us)) {

                    UI ui = getUI().get();
                    ui.getPage().setLocation("activate");
                } else {

                    notification.show("Такой пользователь уже существует!",
                            3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        add(usernameField, passwordField, emailField, button);

    }
}


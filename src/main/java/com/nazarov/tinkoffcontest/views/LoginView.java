package com.nazarov.tinkoffcontest.views;

import com.alexeynovosibirsk.demo.service.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    UserService userService;

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        this.getElement().getStyle().set(
                "background", //"url(https://imgproxy.cdn-tinkoff.ru/meetups_banner_l_x1/aHR0cHM6Ly9hY2RuLnRpbmtvZmYucnUvc3RhdGljL3RlbXAvYTRkN2I0NDYtMWI5NS00YTdhLThiMjYtYmY2OWEzYzQwMGJkLnBuZw)" +
                "url(../images/bg.jpg) no-repeat center center fixed"
        );

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);

        Button buttonRegister = new Button("Регистрация");
        buttonRegister.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonRegister.addClickListener(e -> {

            UI ui = getUI().get();
            ui.getPage().setLocation("register");
        });

        Button buttonHelp = new Button("Помощь");
        buttonHelp.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonHelp.addClickListener(e -> {
            Dialog helpDialog = new Dialog();
            helpDialog.setWidth("430px");
            helpDialog.setHeight("170px");
            helpDialog.setResizable(true);


            Button okButton = new Button ("OK", event -> {
                helpDialog.close();
            });
            okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            okButton.setMinWidth("380px");

            helpDialog.add(new Text("Чтобы войти админом:  Логин - admin, Пароль - 1"));
            helpDialog.add(new Hr());
            helpDialog.add(new Text("Вход в БД:  Логин - admin, без пароля"));
            helpDialog.add(okButton);
            helpDialog.open();
        });

        Button dbButton = new Button("Open H2 DB Console");
        dbButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dbButton.addClickListener(e -> {

            UI ui = getUI().get();
            ui.getPage().open("h2-console");
        });

        add(

                loginForm,
                buttonRegister,
                buttonHelp,
                dbButton
        );
    }

//
//		TextField usernameField = new TextField("Username");
//		usernameField.setClearButtonVisible(true);
//
//		PasswordField passwordField = new PasswordField("Password");
//		passwordField.setClearButtonVisible(true);
//
//		EmailField emailField = new EmailField("Email");
//		emailField.setClearButtonVisible(true);
//
//
//		Button button = new Button("Зарегистрироваться");
//		button.addClickShortcut(Key.ENTER);
//		button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//		button.addClickListener(a -> {
//			User us = new User();
//
//			us.setUsername(usernameField.getValue());
//			us.setPassword(passwordField.getValue());
//			us.setEmail(emailField.getValue());
//			LOGGER.info("PWD: {}", us.getPassword());
//
//
//			userService.addUser(us);
//
//
//					});
//		add(usernameField, passwordField, emailField, button);
//
//}






}
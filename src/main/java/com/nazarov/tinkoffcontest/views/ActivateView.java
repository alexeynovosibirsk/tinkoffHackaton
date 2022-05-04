package com.nazarov.tinkoffcontest.views;

import com.nazarov.tinkoffcontest.entity.User;
import com.nazarov.tinkoffcontest.repository.UserRepository;
import com.nazarov.tinkoffcontest.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("activate")
@PageTitle("Activate")
public class ActivateView extends VerticalLayout {


    @Autowired
    UserRepository userRepository;


    public ActivateView(UserService userService) {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        Notification notification = new Notification();

        this.getElement().getStyle().set(
                "background",
                "url(https://mota.ru/upload/resize/1920/1080/upload/wallpapers/source/2013/10/09/12/01/37640/lL7ZyBxMlF-b8b.jpg)" +
                        "no-repeat center center fixed"
        );

        TextField textField = new TextField("Введите код здесь");

        Button button = new Button("OK");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {

            if(userService.activateUser(textField.getValue())) {

                UI ui = getUI().get();
                ui.getPage().setLocation("login");
            } else {

                notification.show("Неверный код",
                        3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(textField, button);
    }
}


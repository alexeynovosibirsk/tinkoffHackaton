package com.nazarov.tinkoffcontest.views.users;

import com.nazarov.tinkoffcontest.entity.User;
import com.nazarov.tinkoffcontest.security.SecurityUtils;
import com.nazarov.tinkoffcontest.service.UserService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("admin")
public class UsersView extends HorizontalLayout {

        public UsersView(UserService userService) {

            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setAlignItems(Alignment.CENTER);
            this.getElement().getStyle().set(
                    "background",
                    "url(../images/bg.jpg)" +
                            "no-repeat center center fixed"
            );

            String login = SecurityUtils.getLogin();
            String roles = SecurityUtils.getRoles();

            //Grid
            Grid<User> grid = new Grid(User.class);
            grid.setColumns("id", "username", "email", "active");
            //Adding data from another table
            Grid.Column<User> roleCol = grid.addColumn(User::getRoles);
            roleCol.setHeader("role");
//        grid.addColumn(roleCol.getRenderer());
            grid.getColumns().forEach(col -> col.setAutoWidth(true));
            grid.getColumns().forEach(col -> col.setResizable(true));
            grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
            grid.setItems(userService.findAll());

            // Bottom fields (Statistics)
            TextField totalField = new TextField();
            totalField.setValueChangeMode(ValueChangeMode.EAGER);
            totalField.setReadOnly(true);
            totalField.setMinWidth("250px");
            totalField.setValue("Учетных записей всего: " + userService.countAll());

            TextField adminField = new TextField();
            adminField.setValueChangeMode(ValueChangeMode.EAGER);
            adminField.setReadOnly(true);
            adminField.setMinWidth("240px");
            adminField.setValue("Администраторов: " + userService.countAllAdmins());

            TextField userField = new TextField();
            userField.setValueChangeMode(ValueChangeMode.ON_BLUR);
            userField.setReadOnly(true);
            userField.setMinWidth("200px");
            userField.setValue("Пользователей: " + userService.countAllUsers());

            TextField activeField = new TextField();
            activeField.setValueChangeMode(ValueChangeMode.ON_BLUR);
            activeField.setReadOnly(true);
            activeField.setMinWidth("200px");
            activeField.setValue("Aктивных УЗ: " + userService.activeAccounts());

            TextField inactiveField = new TextField();
            inactiveField.setValueChangeMode(ValueChangeMode.ON_BLUR);
            inactiveField.setReadOnly(true);
            inactiveField.setMinWidth("200px");
            inactiveField.setValue("Неактивных УЗ: " + userService.inactiveAccounts());

            HorizontalLayout bottombar = new HorizontalLayout(totalField, activeField, inactiveField, userField, adminField);

            //Admin panel
            TextField findByNameField = new TextField();
            findByNameField.setClearButtonVisible(true);
            findByNameField.setPlaceholder("Имя");
            if(findByNameField.getValue() == null) {
                findByNameField.setValue("'%%'");
            }

            TextField findByEmailField = new TextField();
            findByEmailField.setClearButtonVisible(true);
            findByEmailField.setPlaceholder("Email");
            if(findByEmailField.getValue() == null) {
                findByEmailField.setValue("'%%'");
            }

            Checkbox isActiveCheckbox = new Checkbox();
            isActiveCheckbox.setLabel("Active");
            isActiveCheckbox.setValue(true);

            Button findButton = new Button("Поиск");
            findButton.addClickShortcut(Key.ENTER);
            findButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            findButton.addClickListener(e -> {

                if(isActiveCheckbox.getValue().equals(true)) {

                    grid.setItems(userService.mainSelectActive(findByNameField.getValue(), findByEmailField.getValue()));

                } else {
                    grid.setItems(userService.mainSelectInactive(findByNameField.getValue(), findByEmailField.getValue()));
                }
                grid.getDataProvider().refreshAll();
            });

            Notification notification = new Notification();

            Button activateDeactivateButton = new Button("Активировать-Деактивировать");
            activateDeactivateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            activateDeactivateButton.setMinWidth("300px");
            activateDeactivateButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(grid.asSingleSelect().getValue() == null) {
                        notification.show("Не выбрана учетная запись!",
                                3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                    } else {

                        userService.changeActive(grid.asSingleSelect().getValue(),
                                grid.asSingleSelect().getValue().isActive());
                        if(grid.asSingleSelect().getValue().isActive()) {
                            notification.show("Учетная запись активирована",
                                    3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } else {
                            notification.show("Учетная запись деактивирована",
                                    3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                        grid.getDataProvider().refreshAll();
                        activeField.setValue("Aктивных УЗ: " + userService.activeAccounts());
                        inactiveField.setValue("Не активных УЗ: " + userService.inactiveAccounts());
                    }
                }
            });

            Button changeRoleButton = new Button("Изменить роль");
            changeRoleButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            changeRoleButton.setMinWidth("300px");
            changeRoleButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    if(grid.asSingleSelect().getValue() == null) {
                        notification.show("Не выбрана учетная запись!",
                                3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
                    } else {

                        userService.changeRole(grid.asSingleSelect().getValue(),
                                grid.asSingleSelect().getValue().getRoles());

                        notification.show("Параметр изменен",
                                3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        grid.getDataProvider().refreshAll();
                        userField.setValue("Пользователей: " + userService.countAllUsers());
                        adminField.setValue("Администраторов: " + userService.countAllAdmins());
                    }
                }
            });

            HorizontalLayout adminPanel = new HorizontalLayout(
                    findByNameField, findByEmailField, isActiveCheckbox, findButton, activateDeactivateButton, changeRoleButton);
            adminPanel.setVisible(false);
            if (SecurityUtils.isAdmin()) {
                adminPanel.setVisible(true);
            }

            Button logoutButton = new Button("Выход");
            logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            logoutButton.setMinWidth("300px");
            logoutButton.addClickListener(e -> {
                UI ui = getUI().get();
                ui.getPage().setLocation("login");
                ui.getSession().close();
            });

            VerticalLayout panel = new VerticalLayout();
            panel.setAlignItems(Alignment.CENTER);
            panel.addAndExpand(
                    new H3("Вы вошли как " + login),
                    new H4("Роли учетной записи: " + roles),
                    adminPanel,
                    grid,
                    bottombar,
                    logoutButton
            );

            add(panel);
        }
    }
}

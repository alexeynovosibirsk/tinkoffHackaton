package com.nazarov.tinkoffcontest.security;

import com.nazarov.tinkoffcontest.views.ActivateView;
import com.nazarov.tinkoffcontest.views.LoginView;
import com.nazarov.tinkoffcontest.views.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(BeforeEnterEvent event) {
        if(ActivateView.class.equals(event.getNavigationTarget())) {
            return;
        }
        if(RegisterView.class.equals(event.getNavigationTarget())) {
            return;
        }

        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}
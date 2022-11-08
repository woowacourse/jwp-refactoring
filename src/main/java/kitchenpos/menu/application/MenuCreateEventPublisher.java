package kitchenpos.menu.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuCreateEvent;

@Component
public class MenuCreateEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MenuCreateEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(final Menu menu) {
        MenuCreateEvent event = new MenuCreateEvent(this, menu);
        applicationEventPublisher.publishEvent(event);
    }
}

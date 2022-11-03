package kitchenpos.menu.application.event;

import kitchenpos.menu.domain.Menu;

public class MenuCreatedEvent {

    private final Menu menu;

    public MenuCreatedEvent(final Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }
}

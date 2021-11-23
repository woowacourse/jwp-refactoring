package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.RegistrationInGroupEvent;

public class MenuRegisteredEvent implements RegistrationInGroupEvent {

    private final Menu menu;

    public MenuRegisteredEvent(final Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public Long getMenuGroupId() {
        return menu.getMenuGroupId();
    }
}

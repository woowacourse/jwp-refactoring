package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import org.springframework.context.ApplicationEvent;

public class MenuUpdateEvent {

    private final Menu menu;
    private final Long originalMenuId;
    public MenuUpdateEvent(final Menu menu, final Long menuId) {
        this.menu = menu;
        this.originalMenuId = menuId;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getOriginalMenuId() {
        return originalMenuId;
    }
}

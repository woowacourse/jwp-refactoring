package kitchenpos.menu.domain;

import org.springframework.context.ApplicationEvent;

public class MenuUpdatedEvent {

    private final Long menuId;
    private final Menu updatedMenu;

    public MenuUpdatedEvent(Long menuId, Menu updatedMenu) {
        this.menuId = menuId;
        this.updatedMenu = updatedMenu;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Menu getUpdatedMenu() {
        return updatedMenu;
    }
}

package kitchenpos.menu.domain;

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

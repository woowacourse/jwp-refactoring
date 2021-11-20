package kitchenpos.menu.domain;

public class MenuChangedEvent {
    private Long originalMenuId;
    private Menu menu;

    public MenuChangedEvent(Long originalMenuId, Menu menu) {
        this.originalMenuId = originalMenuId;
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getOriginalMenuId() {
        return originalMenuId;
    }
}

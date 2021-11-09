package kitchenpos.menu.domain;

public class MenuUpdateEvent {

    private final Menu menu;
    private final Long originalId;

    public MenuUpdateEvent(Menu menu, Long originalId) {
        this.menu = menu;
        this.originalId = originalId;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getOriginalId() {
        return originalId;
    }
}

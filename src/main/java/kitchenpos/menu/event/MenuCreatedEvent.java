package kitchenpos.menu.event;

public class MenuCreatedEvent {

    private final Long menuGroupId;

    public MenuCreatedEvent(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}

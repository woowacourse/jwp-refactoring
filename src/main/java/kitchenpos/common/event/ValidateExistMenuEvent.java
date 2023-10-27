package kitchenpos.common.event;

public class ValidateExistMenuEvent {

    private final Long menuId;

    public ValidateExistMenuEvent(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }
}

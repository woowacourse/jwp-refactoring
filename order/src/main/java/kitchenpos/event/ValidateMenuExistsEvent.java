package kitchenpos.event;

public class ValidateMenuExistsEvent {

    private final Long menuId;

    public ValidateMenuExistsEvent(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }
}

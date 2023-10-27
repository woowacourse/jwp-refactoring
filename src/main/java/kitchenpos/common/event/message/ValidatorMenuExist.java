package kitchenpos.common.event.message;

public class ValidatorMenuExist {

    private final Long menuId;

    public ValidatorMenuExist(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }
}

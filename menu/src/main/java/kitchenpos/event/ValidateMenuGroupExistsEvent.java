package kitchenpos.event;

public class ValidateMenuGroupExistsEvent {

    private final Long menuGroupId;

    public ValidateMenuGroupExistsEvent(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}

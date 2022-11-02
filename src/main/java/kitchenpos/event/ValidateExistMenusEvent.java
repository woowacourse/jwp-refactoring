package kitchenpos.event;

import java.util.List;

public class ValidateExistMenusEvent {

    private final List<Long> menuIds;

    public ValidateExistMenusEvent(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}

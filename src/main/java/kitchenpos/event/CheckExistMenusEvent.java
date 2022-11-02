package kitchenpos.event;

import java.util.List;

public class CheckExistMenusEvent {

    private final List<Long> menuIds;

    public CheckExistMenusEvent(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}

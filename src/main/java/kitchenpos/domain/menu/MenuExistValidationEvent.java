package kitchenpos.domain.menu;

import java.util.List;

public class MenuExistValidationEvent {

    private final List<Long> menuIds;

    public MenuExistValidationEvent(final List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}

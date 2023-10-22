package kitchenpos.domain.order;

import java.util.List;

public class OrderMenuExistValidationEvent {

    private final List<Long> menuIds;

    public OrderMenuExistValidationEvent(final List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }
}

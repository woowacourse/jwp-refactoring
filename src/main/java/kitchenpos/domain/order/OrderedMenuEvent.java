package kitchenpos.domain.order;

import java.util.List;

public class OrderedMenuEvent {

    private final List<Long> menuIds;

    public OrderedMenuEvent(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> menuIds() {
        return menuIds;
    }
}

package kitchenpos.ui.dto;

import java.util.List;

public class OrderCreateRequest {

    private final List<Long> menuIds;
    private final List<Integer> quantities;
    private final Long orderTableId;

    public OrderCreateRequest(List<Long> menuIds, List<Integer> quantities, Long orderTableId) {
        this.menuIds = menuIds;
        this.quantities = quantities;
        this.orderTableId = orderTableId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}

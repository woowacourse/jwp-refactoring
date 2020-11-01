package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<MenuQuantityRequest> menuQuantities;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<MenuQuantityRequest> menuQuantities) {
        this.orderTableId = orderTableId;
        this.menuQuantities = menuQuantities;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<MenuQuantityRequest> getMenuQuantities() {
        return menuQuantities;
    }

    public Order toEntity() {
        return new Order(this.orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now());
    }
}

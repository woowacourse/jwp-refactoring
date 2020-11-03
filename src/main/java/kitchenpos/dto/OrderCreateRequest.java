package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {

    @NotNull
    private Long orderTableId;

    @NotEmpty
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

    public Order toEntity(final OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
    }
}

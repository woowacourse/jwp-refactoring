package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

public class OrderRequest {
    @NotNull(groups = OrderValidationGroup.create.class)
    private Long orderTableId;

    @NotNull(groups = OrderValidationGroup.changeOrderStatus.class)
    private OrderStatus orderStatus;

    @NotEmpty(groups = OrderValidationGroup.create.class)
    private List<OrderLineItemDto> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}

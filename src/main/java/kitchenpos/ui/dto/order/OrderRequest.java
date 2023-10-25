package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime now) {
        return new Order(orderTable, orderStatus.name(), now);
    }
}

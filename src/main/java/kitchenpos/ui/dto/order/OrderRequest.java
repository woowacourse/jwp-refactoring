package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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

    public Order toEntity(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime now,
            final List<OrderLineItem> orderLineItems
    ) {
        return Order.of(orderTable, orderStatus.name(), now, orderLineItems);
    }
}

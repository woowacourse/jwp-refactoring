package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.common.vo.OrderStatus;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.OrderTable;

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
            final OrderLineItems orderLineItems
    ) {
        return Order.of(orderTable, OrderStatus.valueOf(orderStatus.name()), now, orderLineItems);
    }
}

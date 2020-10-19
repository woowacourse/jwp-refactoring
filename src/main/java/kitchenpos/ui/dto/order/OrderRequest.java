package kitchenpos.ui.dto.order;

import com.google.common.collect.Lists;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() { }

    public OrderRequest(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItemRequest> orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Lists.newArrayList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}

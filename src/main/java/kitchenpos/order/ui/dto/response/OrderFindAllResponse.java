package kitchenpos.order.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderFindAllResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemFindAllResponse> orderLineItems;

    public OrderFindAllResponse() {
    }

    public OrderFindAllResponse(final Long id, final Long orderTableId, final String orderStatus,
                                final LocalDateTime orderedTime,
                                final List<OrderLineItemFindAllResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderFindAllResponse of(final Order order, final List<OrderLineItem> orderLineItems) {
        return new OrderFindAllResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), OrderLineItemFindAllResponse.from(orderLineItems));
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemFindAllResponse> getOrderLineItems() {
        return orderLineItems;
    }
}

package kitchenpos.factory;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;

public class OrderFactory {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private OrderFactory() {

    }

    private OrderFactory(Long id,
                         Long orderTableId,
                         OrderStatus orderStatus,
                         LocalDateTime orderedTime,
                         List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderFactory builder() {
        return new OrderFactory();
    }

    public static OrderFactory copy(Order order) {
        return new OrderFactory(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            order.getOrderLineItems()
        );
    }

    public static OrderRequest dto(Order order) {
        return new OrderRequest(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            OrderLineItemFactory.dtoList(order.getOrderLineItems())
        );
    }

    public OrderFactory id(Long id) {
        this.id = id;
        return this;
    }

    public OrderFactory orderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
        return this;
    }

    public OrderFactory orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderFactory orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public OrderFactory orderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        return this;
    }

    public Order build() {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}

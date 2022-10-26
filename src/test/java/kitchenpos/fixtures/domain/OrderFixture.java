package kitchenpos.fixtures.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderFixture {

    public static Order createOrder(final Long orderTableId, final OrderStatus orderStatus,
                                    final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static class OrderRequestBuilder {

        private final List<OrderLineItem> orderLineItems = new ArrayList<>();

        private Long orderTableId;
        private String orderStatus = OrderStatus.COOKING.name();
        private LocalDateTime orderedTime = LocalDateTime.now();

        public OrderRequestBuilder orderTableId(final Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderRequestBuilder orderStatus(final String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderRequestBuilder orderStatus(final OrderStatus orderStatus) {
            this.orderStatus = orderStatus.name();
            return this;
        }

        public OrderRequestBuilder orderedTime(final LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderRequestBuilder addOrderLineItem(final OrderLineItem... orderLineItems) {
            this.orderLineItems.addAll(List.of(orderLineItems));
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(orderStatus);
            order.setOrderedTime(orderedTime);
            order.setOrderLineItems(orderLineItems);

            return order;
        }
    }
}

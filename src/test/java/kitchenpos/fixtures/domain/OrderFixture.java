package kitchenpos.fixtures.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;

public class OrderFixture {

    public static Order createOrder(final Long orderTableId, final OrderStatus orderStatus,
                                    final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.changeOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static class OrderRequestBuilder {

        private final List<OrderLineItem> orderLineItems = new ArrayList<>();

        private Long orderTableId;
        private OrderStatus orderStatus = OrderStatus.COOKING;
        private LocalDateTime orderedTime = LocalDateTime.now();

        public OrderRequestBuilder orderTableId(final Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderRequestBuilder orderStatus(final OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
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

        public OrderRequest build() {
            List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                    .map(OrderLineItemRequest::new)
                    .collect(Collectors.toList());

            return new OrderRequest(orderTableId, orderLineItemRequests);
        }
    }
}

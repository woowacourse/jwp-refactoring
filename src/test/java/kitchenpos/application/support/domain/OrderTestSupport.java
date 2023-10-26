package kitchenpos.application.support.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class OrderTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private OrderTable orderTable = OrderTableTestSupport.builder().build();
        private OrderStatus orderStatus = OrderStatus.COOKING;
        private LocalDateTime orderedTime = LocalDateTime.now();
        private List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemTestSupport.builder().build(),
                OrderLineItemTestSupport.builder().build(),
                OrderLineItemTestSupport.builder().build()
        );

        public Builder orderTable(final OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(final OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(final LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(final List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(orderTable,orderedTime);
        }

        public OrderCreateRequest buildToOrderCreateRequest() {
            final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                    .map(it -> new OrderLineItemRequest(it.getMenu().getId(), it.getQuantity()))
                    .collect(Collectors.toList());

            return new OrderCreateRequest(orderTable.getId(), orderLineItemRequests);
        }
    }
}

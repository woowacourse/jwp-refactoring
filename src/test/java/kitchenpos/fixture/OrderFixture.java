package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrderFixture {

    public static Order orderForCreate(long tableId, OrderLineItem... orderLineItems) {
        return builder()
                .orderLineItems(orderLineItems)
                .orderTableId(tableId)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Order build() {
            Order order = new Order();
            order.setId(id);
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(orderStatus);
            order.setOrderedTime(orderedTime);
            order.setOrderLineItems(orderLineItems);
            return order;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(OrderLineItem... orderLineItems) {
            return orderLineItems(Arrays.asList(orderLineItems));
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }
    }
}

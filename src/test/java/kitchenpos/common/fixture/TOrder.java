package kitchenpos.common.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Or;

public class TOrder {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public void id(Long id) {
            this.id = id;
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

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.setId(id);
            order.setOrderTableId(orderTableId);
            order.setOrderStatus(orderStatus);
            order.setOrderedTime(orderedTime);
            order.setOrderLineItems(orderLineItems);

            return order;
        }
    }
}

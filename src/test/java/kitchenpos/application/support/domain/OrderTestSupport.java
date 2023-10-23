package kitchenpos.application.support.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private Long orderTableId = OrderTableTestSupport.builder().build().getId();
        private String orderStatus = OrderStatus.COOKING.name();
        private LocalDateTime orderedTime = LocalDateTime.now();
        private List<OrderLineItem> orderLineItems = List.of(
                OrderLineItemTestSupport.builder().OrderId(id).build(),
                OrderLineItemTestSupport.builder().OrderId(id).build(),
                OrderLineItemTestSupport.builder().OrderId(id).build()
        );

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTableId(final Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(final String orderStatus) {
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
            final var result = new Order();
            result.setId(id);
            result.setOrderTableId(orderTableId);
            result.setOrderStatus(orderStatus);
            result.setOrderedTime(orderedTime);
            result.setOrderLineItems(orderLineItems);
            return result;
        }

        public OrderCreateRequest buildToOrderCreateRequest() {
            final List<OrderLineItemDto> orderLineItemDtos = orderLineItems.stream()
                    .map(it -> new OrderLineItemDto(it.getMenuId(), it.getQuantity()))
                    .collect(Collectors.toList());

            return new OrderCreateRequest(orderTableId, orderLineItemDtos);
        }
    }
}

package kitchenpos.application.dto.response;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private CreateOrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static CreateOrderResponse of(Order order, List<OrderLineItemResponse> orderLineItems) {
        return builder()
                .id(order.getId())
                .orderTableId(order.getOrderTableId())
                .orderStatus(order.getOrderStatus().name())
                .orderedTime(order.getOrderedTime())
                .orderLineItems(orderLineItems)
                .build();
    }

    public static CreateOrderResponseBuilder builder() {
        return new CreateOrderResponseBuilder();
    }

    public static final class CreateOrderResponseBuilder {
        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItemResponse> orderLineItems;

        private CreateOrderResponseBuilder() {
        }

        public CreateOrderResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateOrderResponseBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public CreateOrderResponseBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public CreateOrderResponseBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public CreateOrderResponseBuilder orderLineItems(List<OrderLineItemResponse> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public CreateOrderResponse build() {
            return new CreateOrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}

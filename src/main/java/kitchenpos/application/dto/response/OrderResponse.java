package kitchenpos.application.dto.response;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final String orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime.toString();
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return builder()
                .id(order.getId())
                .orderTableId(order.getOrderTable().getId())
                .orderStatus(order.getOrderStatus().name())
                .orderedTime(order.getOrderedTime())
                .orderLineItems(order.getOrderLineItems().stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public static OrderResponseBuilder builder() {
        return new OrderResponseBuilder();
    }

    public static final class OrderResponseBuilder {
        private Long id;
        private Long orderTableId;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItemResponse> orderLineItems;

        private OrderResponseBuilder() {
        }

        public OrderResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderResponseBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderResponseBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderResponseBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderResponseBuilder orderLineItems(List<OrderLineItemResponse> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public OrderResponse build() {
            return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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

    public String getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}

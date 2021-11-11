package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                          List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                OrderLineItemResponse.listOf(order.getOrderLineItems()));
    }

    public static List<OrderResponse> listOf(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
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

    public static class OrderLineItemResponse {
        private final Long menuId;
        private final long quantity;

        private OrderLineItemResponse(Long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
        }

        public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems) {
            return orderLineItems.stream().map(OrderLineItemResponse::of).collect(Collectors.toList());
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}

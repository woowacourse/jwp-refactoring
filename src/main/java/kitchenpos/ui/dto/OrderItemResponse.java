package kitchenpos.ui.dto;

import kitchenpos.domain.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderItemResponse() {
    }

    private OrderItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getSeq(),
                orderItem.getOrderId(),
                orderItem.getMenuId(),
                orderItem.getQuantity()
        );
    }

    public static List<OrderItemResponse> from(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}

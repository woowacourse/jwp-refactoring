package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
            Long id,
            Long orderTableId,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
                        .map(OrderLineItemResponse::from)
                        .collect(Collectors.toList())
        );
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

        private Long seq;
        private MenuSnapShot menuSnapShot;
        private long quantity;

        public OrderLineItemResponse(Long seq, MenuSnapShot menuSnapShot, long quantity) {
            this.seq = seq;
            this.menuSnapShot = menuSnapShot;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(
                    orderLineItem.getSeq(),
                    orderLineItem.getMenuSnapShot(),
                    orderLineItem.getQuantity()
            );
        }

        public Long getSeq() {
            return seq;
        }

        public MenuSnapShot getMenuSnapShot() {
            return menuSnapShot;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}

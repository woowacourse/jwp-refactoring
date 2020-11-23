package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItemDtos) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderResponse from(Order order) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTable().getId();
        String orderStatus = order.getOrderStatus().name();
        LocalDateTime orderedTime = order.getOrderedTime();
        List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems().stream()
                .map(OrderLineItemDto::from)
                .collect(Collectors.toList());

        return new OrderResponse(id, orderTableId, orderStatus, orderedTime, orderLineItemDtos);
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

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }

    public static class OrderLineItemDto {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemDto from(OrderLineItem orderLineItem) {
            Long seq = orderLineItem.getSeq();
            Long orderId = orderLineItem.getOrder().getId();
            Long menuId = orderLineItem.getMenu().getId();
            long quantity = orderLineItem.getQuantity();

            return new OrderLineItemDto(seq, orderId, menuId, quantity);
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
}

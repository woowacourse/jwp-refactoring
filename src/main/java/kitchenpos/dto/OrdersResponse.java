package kitchenpos.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Orders;

public class OrdersResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderTime;
    private final List<OrderLineItemDto> orderLineItems;

    private OrdersResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderTime,
            List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrdersResponse from(Orders orders) {
        return new OrdersResponse(orders.getId(), orders.getOrderTable().getId(),
                orders.getOrderStatus(), orders.getOrderedTime(),
                orders.getOrderLineItems().stream().map(OrderLineItemDto::from).collect(
                        toList()));
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

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemDto {

        private final Long seq;
        private final Long menuId;
        private final Long quantity;

        private OrderLineItemDto(Long seq, Long menuId, Long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemDto from(OrderLineItem orderLineItem) {
            return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getMenu().getId(),
                    orderLineItem.getQuantity());
        }

        public Long getSeq() {
            return seq;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}

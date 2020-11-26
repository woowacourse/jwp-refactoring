package kitchenpos.ui.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItemResponse(seq, orderId, menuId, quantity);
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem, Long orderId) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderId,
            orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems,
        Long orderId) {
        return orderLineItems.stream()
            .map(orderLineItem -> of(orderLineItem, orderId))
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return quantity == that.quantity &&
            Objects.equals(seq, that.seq) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, orderId, menuId, quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{" +
            "seq=" + seq +
            ", orderId=" + orderId +
            ", menuId=" + menuId +
            ", quantity=" + quantity +
            '}';
    }
}

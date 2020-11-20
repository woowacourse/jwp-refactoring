package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

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

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        Long seq = orderLineItem.getSeq();
        Long orderId = orderLineItem.getOrder().getId();
        Long menuId = orderLineItem.getMenu().getId();
        long quantity = orderLineItem.getQuantity();

        return new OrderLineItemResponse(seq, orderId, menuId, quantity);
    }

    public static List<OrderLineItemResponse> toResponseList(
        List<OrderLineItem> savedOrderLineItems) {
        return savedOrderLineItems.stream()
            .map(OrderLineItemResponse::of)
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

package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemDetailResponse {
    private Long seq;
    private Long orderId;
    private long quantity;
    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;

    private OrderLineItemDetailResponse() {}

    public OrderLineItemDetailResponse(Long seq, Long orderId, long quantity, Long menuId, String menuName, BigDecimal menuPrice) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemDetailResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemDetailResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getQuantity(),
                orderLineItem.getMenuId(),
                orderLineItem.getMenu().getName(),
                orderLineItem.getMenu().getPrice()
        );
    }

    public static List<OrderLineItemDetailResponse> from(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemDetailResponse::from)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}

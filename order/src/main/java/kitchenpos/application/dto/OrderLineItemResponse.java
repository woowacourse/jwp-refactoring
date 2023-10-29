package kitchenpos.application.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;

    private Long orderId;

    private Long menuId;

    private long quantity;

    private String productName;

    private int productPrice;

    public OrderLineItemResponse(final Long seq,
                                 final Long orderId,
                                 final Long menuId,
                                 final long quantity,
                                 final String productName,
                                 final int productPrice) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(),
            orderLineItem.getOrder().getId(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity(),
            orderLineItem.getName(),
            orderLineItem.getPrice());
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

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }
}

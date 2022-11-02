package kitchenpos.order.dto;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final long quantity;
    private final String menuName;
    private final BigDecimal menuPrice;

    public OrderLineItemResponse(Long seq, long quantity, String menuName, BigDecimal menuPrice) {
        this.seq = seq;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getQuantity(),
            orderLineItem.getMenuName(), orderLineItem.getMenuPrice());
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}

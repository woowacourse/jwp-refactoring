package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private final Long seq;
    private final String menuName;
    private final BigDecimal price;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final String menuName, final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuName(),
                orderLineItem.getPrice().getPrice(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}

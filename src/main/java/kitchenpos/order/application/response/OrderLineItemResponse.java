package kitchenpos.order.application.response;

import java.math.BigDecimal;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final String menuName;
    private final BigDecimal price;
    private final long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getId();
        this.menuName = orderLineItem.getMenuName();
        this.price = orderLineItem.getPrice().getValue();
        this.quantity = orderLineItem.getQuantity();
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

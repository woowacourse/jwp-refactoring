package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItem(final Long orderId, final long quantity, final String menuName, final BigDecimal menuPrice) {
        this(null, orderId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(final Long seq, final Long orderId, final String menuName, final BigDecimal menuPrice,
                         final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final String menuName;
    private final Price menuPrice;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = new Price(menuPrice);
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, String menuName, BigDecimal menuPrice, long quantity) {
        this(null, orderId, menuName, menuPrice, quantity);
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
        return menuPrice.getAmount();
    }

    public long getQuantity() {
        return quantity;
    }
}

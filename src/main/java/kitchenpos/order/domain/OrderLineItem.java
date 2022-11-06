package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;

public class OrderLineItem {
    private final Long seq;
    private final Long orderId;
    private final String menuName;
    private final Price menuPrice;
    private final long quantity;

    public OrderLineItem(Long orderId, String menuName, BigDecimal menuPrice, long quantity) {
        this(null, orderId, menuName, new Price(menuPrice), quantity);
    }

    public OrderLineItem(Long seq, Long orderId, String menuName, Price menuPrice, long quantity) {
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

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getValue();
    }
}

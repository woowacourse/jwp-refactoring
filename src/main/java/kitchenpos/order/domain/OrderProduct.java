package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.menu.domain.MenuProduct;

public class OrderProduct {

    private Long seq;
    private Long orderMenuId;
    private final String name;
    private final BigDecimal price;

    public OrderProduct(final MenuProduct menuProduct) {
        this.name = menuProduct.getProductName();
        this.price = menuProduct.getPrice();
    }

    public OrderProduct(final Long seq, final long orderMenuId, final String name, final BigDecimal price) {
        this.seq = seq;
        this.orderMenuId = orderMenuId;
        this.name = name;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setOrderMenuId(final Long orderMenuId) {
        this.orderMenuId = orderMenuId;
    }
}

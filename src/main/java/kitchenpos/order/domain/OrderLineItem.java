package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final String name, final BigDecimal price, final long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(final String name, final BigDecimal price, final long quantity) {
        this(null, name, price, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}

package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItem(final Long seq, final Long orderId, final String name, final BigDecimal price,
                         final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderId, final String name, final BigDecimal price, final long quantity) {
        this(null, orderId, name, price, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}

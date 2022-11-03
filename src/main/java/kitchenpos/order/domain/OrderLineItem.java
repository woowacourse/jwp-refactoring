package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final String name;
    private final BigDecimal price;
    private final long quantity;

    public OrderLineItem(Long seq, Long orderId, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItem(Long orderId, String name, BigDecimal price, long quantity) {
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

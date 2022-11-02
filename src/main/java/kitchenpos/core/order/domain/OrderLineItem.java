package kitchenpos.core.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private long quantity;

    private String name;
    private BigDecimal price;

    private OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Long orderId, final long quantity, final String name,
                         final BigDecimal price) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public OrderLineItem(final Long orderId, final long quantity, final String name, final BigDecimal price) {
        this.orderId = orderId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItem(final Long seq,
                         final Long orderId,
                         final String name,
                         final BigDecimal price,
                         final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItem ofNullSeq(final Long orderId,
                                          final String name,
                                          final BigDecimal price,
                                          final long quantity) {
        return new OrderLineItem(null, orderId, name, price, quantity);
    }

    public void updateOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public void updateMenuInformation(final Menu menu) {
        this.name = menu.getName();
        this.price = menu.getPrice().multiply(BigDecimal.valueOf(this.quantity));
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

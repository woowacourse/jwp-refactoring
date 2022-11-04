package kitchenpos.core.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private OrderMenu orderMenu;

    private OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Long orderId, final OrderMenu orderMenu) {
        this.seq = seq;
        this.orderId = orderId;
        this.orderMenu = orderMenu;
    }

    public OrderLineItem(final Long seq,
                         final Long orderId,
                         final long quantity,
                         final Long menuId,
                         final String name,
                         final BigDecimal price) {
        this(seq, orderId, new OrderMenu(quantity, menuId, name, price));
    }

    public OrderLineItem(final Long orderId,
                         final long quantity,
                         final Long menuId,
                         final String name,
                         final BigDecimal price) {
        this(null, orderId, quantity, menuId, name, price);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return orderMenu.getQuantity();
    }

    public Long getMenuId() {
        return orderMenu.getMenuId();
    }

    public String getName() {
        return orderMenu.getName();
    }

    public BigDecimal getPrice() {
        return orderMenu.getPrice();
    }
}

package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private BigDecimal menuPrice;
    private String menuName;
    private long quantity;

    public OrderLineItem(final Long seq, final Long orderId,
                         final Long menuId, final BigDecimal menuPrice,
                         final String menuName, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuPrice = menuPrice;
        this.menuName = menuName;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long seq, final Long menuId,
                         final BigDecimal menuPrice, final String menuName,
                         final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuPrice = menuPrice;
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void addOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public long getQuantity() {
        return quantity;
    }
}

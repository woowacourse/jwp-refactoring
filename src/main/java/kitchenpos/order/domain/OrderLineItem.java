package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    public OrderLineItem(Long seq, Long orderId, long quantity, String menuName, BigDecimal menuPrice) {
        this.seq = seq;
        this.orderId = orderId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public OrderLineItem(Long orderId, long quantity, String menuName, BigDecimal menuPrice) {
        this(null, orderId, quantity, menuName, menuPrice);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(BigDecimal menuPrice) {
        this.menuPrice = menuPrice;
    }
}

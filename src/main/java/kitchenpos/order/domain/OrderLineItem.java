package kitchenpos.order.domain;

import java.math.BigDecimal;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final String menu_name;
    private final BigDecimal menu_price;
    private final long quantity;

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final String menu_name,
                         final BigDecimal menu_price, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderId, final Long menuId, final String menuName, final BigDecimal menuPrice,
                         final long quantity) {
        this(null, orderId, menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, null, null, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menu_name;
    }

    public BigDecimal getMenuPrice() {
        return menu_price;
    }

    public long getQuantity() {
        return quantity;
    }
}

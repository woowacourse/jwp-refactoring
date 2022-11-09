package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final String menuName;
    private final Price menuPrice;
    private final long quantity;

    public OrderLineItem(Long orderId, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this(null, orderId, menuId, menuName, menuPrice, quantity);
    }

    public OrderLineItem(Long seq, Long orderId, Long menuId, String menuName, BigDecimal menuPrice, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = new Price(menuPrice);
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, null, null, quantity);
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
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.getValue();
    }

    public long getQuantity() {
        return quantity;
    }
}

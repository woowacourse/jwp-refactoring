package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;

public class OrderLineItem {

    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;
    private final String menuName;
    private final Price menuPrice;

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity,
        final String menuName, final BigDecimal menuPrice) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = new Price(menuPrice);
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity, null, new BigDecimal(0));
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

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice.get();
    }
}

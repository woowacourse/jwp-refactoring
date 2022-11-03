package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.vo.Price;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private String menuName;
    private Price menuPrice;
    private long quantity;

    private OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Long orderId, final String menuName, final Price menuPrice,
                         final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
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

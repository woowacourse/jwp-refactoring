package kitchenpos.ui.dto;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private final Long seq;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final String menuName, final BigDecimal menuPrice,
                                 final long quantity) {
        this.seq = seq;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}

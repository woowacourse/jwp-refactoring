package kitchenpos.dto.order.response;

import java.math.BigDecimal;

public class OrderLineItemResponse {

    private Long seq;
    private long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    private OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final long quantity, final String menuName,
                                 final BigDecimal menuPrice) {
        this.seq = seq;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}

package kitchenpos.dto.response;

import java.math.BigDecimal;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private String menuName;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(final Long seq, final Long orderId, final String menuName, final BigDecimal menuPrice, final long quantity) {
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
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}

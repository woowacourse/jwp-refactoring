package kitchenpos.order.dto;

import java.math.BigDecimal;

public class OrderLineItemRequest {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private BigDecimal menuPrice;
    private String menuName;
    private int quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long seq, final Long orderId, final Long menuId,
                                final BigDecimal menuPrice, final String menuName, final int quantity) {
        this.seq = seq;
        this.orderId = orderId;
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

    public Long getMenuId() {
        return menuId;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getQuantity() {
        return quantity;
    }
}

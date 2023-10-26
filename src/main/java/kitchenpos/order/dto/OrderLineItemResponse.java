package kitchenpos.order.dto;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long menuId;
    private final long quantity;
    private final String menuName;
    private final BigDecimal menuPrice;

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(),
            orderLineItem.getMenuId(),
            orderLineItem.getQuantity(),
            orderLineItem.getMenuName(),
            orderLineItem.getMenuPrice().getAmount()
        );
    }

    public OrderLineItemResponse(Long seq, Long menuId, long quantity, String menuName, BigDecimal menuPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getSeq() {
        return seq;
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
        return menuPrice;
    }
}

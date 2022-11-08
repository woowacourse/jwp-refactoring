package kitchenpos.order.dto.response;

import java.math.BigDecimal;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final Long menuId;
    private final String menuName;
    private final BigDecimal menuPrice;
    private final long quantity;

    public OrderLineItemResponse(final OrderLineItem orderLineItem) {
        this(
                orderLineItem.getSeq(),
                orderLineItem.getOrderedMenu().getMenuId(),
                orderLineItem.getOrderedMenu().getMenuName(),
                orderLineItem.getOrderedMenu().getMenuPrice(),
                orderLineItem.getQuantity()
        );
    }

    public OrderLineItemResponse(final Long seq, final Long menuId, final String menuName, final BigDecimal menuPrice,
                                 final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
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

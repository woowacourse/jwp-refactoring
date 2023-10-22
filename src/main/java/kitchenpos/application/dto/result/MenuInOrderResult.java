package kitchenpos.application.dto.result;

import kitchenpos.domain.order.OrderLineItem;

public class MenuInOrderResult {

    private final Long seq;
    private final Long menuIds;
    private final Long quantity;

    public MenuInOrderResult(final Long seq, final Long menuIds, final Long quantity) {
        this.seq = seq;
        this.menuIds = menuIds;
        this.quantity = quantity;
    }

    public static MenuInOrderResult from(final OrderLineItem orderLineItems) {
        return new MenuInOrderResult(
                orderLineItems.getSeq(),
                orderLineItems.getMenuId(),
                orderLineItems.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuIds() {
        return menuIds;
    }

    public Long getQuantity() {
        return quantity;
    }
}

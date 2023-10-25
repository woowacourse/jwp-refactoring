package kitchenpos.application.dto;

import kitchenpos.domain.order.OrderLineItem;

public class ReadOrderLineItemDto {

    private Long seq;
    private Long menuId;
    private long quantity;

    public ReadOrderLineItemDto(final OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
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
}

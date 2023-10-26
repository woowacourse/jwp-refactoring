package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long seq;
    private Long menuId;
    private Long quantity;

    public OrderLineItemDto(Long seq, Long menuId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        Long seq = orderLineItem.getSeq();
        Long menuId = orderLineItem.getMenu().getId();
        Long quantity = orderLineItem.getQuantity();
        return new OrderLineItemDto(seq, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}

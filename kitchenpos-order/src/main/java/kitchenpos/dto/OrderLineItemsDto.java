package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemsDto {

    private final Long seq;
    private final Long orderId;
    private Long menuId;
    private final Long quantity;

    private OrderLineItemsDto(Long seq, Long orderId, Long menuId, Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemsDto from(final OrderLineItem orderLineItem) {
        return new OrderLineItemsDto(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().getValue()
        );
    }

    public void changeToOrderMenuId(final Long menuId) {
        this.menuId = menuId;
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

    public Long getQuantity() {
        return quantity;
    }
}

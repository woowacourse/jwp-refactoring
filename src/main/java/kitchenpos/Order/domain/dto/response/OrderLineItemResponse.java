package kitchenpos.Order.domain.dto.response;

import kitchenpos.Order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    protected OrderLineItemResponse() {
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

    public static OrderLineItemResponse toDTO(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }
}

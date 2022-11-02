package kitchenpos.order.dto.response;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        seq = orderLineItem.getSeq();
        menuId = orderLineItem.getMenuId();
        quantity = orderLineItem.getQuantity();
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

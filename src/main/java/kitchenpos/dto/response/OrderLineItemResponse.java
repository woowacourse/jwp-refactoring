package kitchenpos.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        seq = orderLineItem.getSeq();
        menu = new MenuResponse(orderLineItem.getMenu());
        quantity = orderLineItem.getQuantity();
    }
}

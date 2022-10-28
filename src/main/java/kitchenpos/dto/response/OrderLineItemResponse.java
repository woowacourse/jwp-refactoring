package kitchenpos.dto.response;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menuResponse;
    private long quantity;

    private OrderLineItemResponse(final Long seq, final MenuResponse menuResponse, final long quantity) {
        this.seq = seq;
        this.menuResponse = menuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), MenuResponse.of(orderLineItem.getMenu()),
                orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResponse getMenuResponse() {
        return menuResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}

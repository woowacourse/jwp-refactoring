package kitchenpos.application.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    @JsonProperty("seq")
    private Long seq;
    @JsonProperty("menuId")
    private Long menuId;
    @JsonProperty("quantity")
    private long quantity;

    private OrderLineItemResponse(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.seq(),
                orderLineItem.menu().id(),
                orderLineItem.quantity()
        );
    }

    public Long seq() {
        return seq;
    }

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity;
    }
}

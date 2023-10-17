package kitchenpos.application.dto.orderlineitem;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    @JsonProperty("seq")
    private Long seq;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("menuId")
    private Long menuId;
    @JsonProperty("quantity")
    private long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.seq(),
                orderLineItem.order().id(),
                orderLineItem.menu().id(),
                orderLineItem.quantity()
        );
    }

    public Long seq() {
        return seq;
    }

    public Long orderId() {
        return orderId;
    }

    public Long menuId() {
        return menuId;
    }

    public long quantity() {
        return quantity;
    }
}

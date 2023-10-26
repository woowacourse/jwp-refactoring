package kitchenpos.order.dto;

import java.util.List;

public class OrderCreateEvent {

    private final List<OrderLineItemsDto> orderLineItemsDtos;
    private final Long orderId;

    public OrderCreateEvent(List<OrderLineItemsDto> orderLineItemsDtos, Long orderId) {
        this.orderLineItemsDtos = orderLineItemsDtos;
        this.orderId = orderId;
    }

    public List<OrderLineItemsDto> getOrderLineItemsDtos() {
        return orderLineItemsDtos;
    }
}

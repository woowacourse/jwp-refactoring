package order.dto;

import java.util.List;

public class CreatedOrderEvent {

    private final List<OrderLineItemsDto> orderLineItemsDtos;
    private final Long orderId;

    public CreatedOrderEvent(List<OrderLineItemsDto> orderLineItemsDtos, Long orderId) {
        this.orderLineItemsDtos = orderLineItemsDtos;
        this.orderId = orderId;
    }

    public List<OrderLineItemsDto> getOrderLineItemsDtos() {
        return orderLineItemsDtos;
    }
}

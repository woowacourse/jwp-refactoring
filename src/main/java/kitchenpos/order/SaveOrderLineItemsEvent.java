package kitchenpos.order;

import kitchenpos.order.application.request.OrderLineItemDto;

import java.util.List;

public class SaveOrderLineItemsEvent {

    private final List<OrderLineItemDto> orderLineItemsDto;
    private final Order order;

    public SaveOrderLineItemsEvent(final List<OrderLineItemDto> orderLineItemsDto, final Order order) {
        this.orderLineItemsDto = orderLineItemsDto;
        this.order = order;
    }

    public List<OrderLineItemDto> getOrderLineItemsDto() {
        return orderLineItemsDto;
    }

    public Order getOrder() {
        return order;
    }
}

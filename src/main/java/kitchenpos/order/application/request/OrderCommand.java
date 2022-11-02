package kitchenpos.order.application.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.ui.request.OrderLineItemRequest;

public class OrderCommand {

    private final Long orderTableId;
    private final List<OrderLineItemCommand> orderLineItems;

    public OrderCommand(Long orderTableId, List<OrderLineItemCommand> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderCommand from(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCommand(orderTableId,
                orderLineItems.stream()
                        .map(it -> new OrderLineItemCommand(it.getMenuId(), it.getQuantity()))
                        .collect(Collectors.toList()));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCommand> getOrderLineItems() {
        return orderLineItems;
    }
}

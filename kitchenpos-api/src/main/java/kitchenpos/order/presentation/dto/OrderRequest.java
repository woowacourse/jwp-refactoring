package kitchenpos.order.presentation.dto;

import java.util.List;
import kitchenpos.order.application.dto.request.OrderCommand;
import kitchenpos.order.application.dto.request.OrderLineItemCommand;

public record OrderRequest(Long orderTableId,
                           List<OrderLineItemRequest> orderLineItems) {
    public OrderCommand toCommand() {
        List<OrderLineItemCommand> orderLineItemCommands = orderLineItems.stream()
                .map(OrderLineItemRequest::toCommand)
                .toList();

        return new OrderCommand(orderTableId, orderLineItemCommands);
    }

}

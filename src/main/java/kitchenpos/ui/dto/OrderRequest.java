package kitchenpos.ui.dto;

import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.application.dto.request.OrderLineItemCommand;

public record OrderRequest(Long orderTableId,
                           List<OrderLineItemRequest> orderLineItems) {
    public OrderCommand toCommand() {
        List<OrderLineItemCommand> orderLineItemCommands = orderLineItems.stream()
                .map(OrderLineItemRequest::toCommand)
                .toList();

        return new OrderCommand(orderTableId, orderLineItemCommands);
    }

}

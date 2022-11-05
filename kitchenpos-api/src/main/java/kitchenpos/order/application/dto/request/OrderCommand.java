package kitchenpos.order.application.dto.request;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;

public record OrderCommand(Long orderTableId,
                           List<OrderLineItemCommand> orderLineItems) {

    public List<OrderLineItem> toOrderLineItems() {
        return orderLineItems.stream()
                .map(OrderLineItemCommand::toEntity)
                .toList();
    }
}

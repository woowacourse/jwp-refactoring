package kitchenpos.application.command;

import static java.util.stream.Collectors.*;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.model.order.OrderLineItem;

public class CreateOrderCommand {
    @NotNull
    private Long orderTableId;
    @NotEmpty
    private List<OrderLineItemRequest> orderLineItems;

    private CreateOrderCommand() {
    }

    public CreateOrderCommand(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(orderLineItemRequest -> new OrderLineItem(orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(toList());
    }
}

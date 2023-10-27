package kitchenpos.order.service.dto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateCommand {

    private final Long orderTableId;
    private final List<OrderLineItemCreateCommand> orderLineItemCreateCommands;

    public OrderCreateCommand(Long orderTableId, List<OrderLineItemCreateCommand> orderLineItemCreateCommands) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreateCommands = orderLineItemCreateCommands;
    }

    public List<Long> menuIds() {
        return orderLineItemCreateCommands.stream()
                .map(OrderLineItemCreateCommand::menuId)
                .collect(Collectors.toList());
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateCommand> orderLineItemCreateCommands() {
        return orderLineItemCreateCommands;
    }
}

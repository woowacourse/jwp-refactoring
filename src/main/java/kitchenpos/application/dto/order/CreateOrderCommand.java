package kitchenpos.application.dto.order;

import java.util.List;
import kitchenpos.application.dto.orderlineitem.OrderLineItemCommand;

public class CreateOrderCommand {

    private Long orderTableId;
    private List<OrderLineItemCommand> orderLineItemCommands;

    public CreateOrderCommand(Long orderTableId, List<OrderLineItemCommand> orderLineItemCommands) {
        this.orderTableId = orderTableId;
        this.orderLineItemCommands = orderLineItemCommands;
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCommand> orderLineItemCommands() {
        return orderLineItemCommands;
    }
}

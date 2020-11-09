package kitchenpos.application.command;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.model.order.Order;
import kitchenpos.domain.model.order.OrderLineItem;

public class CreateOrderCommand {
    @NotNull
    private Long orderTableId;
    @NotEmpty
    private List<OrderLineItem> orderLineItems;

    private CreateOrderCommand() {
    }

    public CreateOrderCommand(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return new Order(null, orderTableId, null, null, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}

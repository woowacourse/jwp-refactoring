package kitchenpos.application.dto.request;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderCommand {

    private final Long orderTableId;
    private final List<OrderLineItem> orderLineItems;

    public OrderCommand(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}

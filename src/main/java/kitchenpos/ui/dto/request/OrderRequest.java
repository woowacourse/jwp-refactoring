package kitchenpos.ui.dto.request;

import java.util.List;
import kitchenpos.application.dto.request.OrderCommand;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderCommand toCommand() {
        return new OrderCommand(orderTableId, orderLineItems);
    }
}

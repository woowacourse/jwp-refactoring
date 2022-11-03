package kitchenpos.ui.jpa.dto.order;

import java.util.List;
import kitchenpos.domain.entity.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
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

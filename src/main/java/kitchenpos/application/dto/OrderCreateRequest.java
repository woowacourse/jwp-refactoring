package kitchenpos.application.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.entity.Order;

public class OrderCreateRequest {
    @NotNull
    private Long orderTableId;
    @NotEmpty
    private List<OrderLineItem> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItem> orderLineItems) {
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

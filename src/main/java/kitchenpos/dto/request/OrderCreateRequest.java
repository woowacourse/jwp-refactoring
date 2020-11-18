package kitchenpos.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.validator.OrderTableEmptyValidate;
import kitchenpos.validator.OrderLineItemCountValidate;

public class OrderCreateRequest {
    @OrderTableEmptyValidate
    private final Long orderTableId;

    @OrderLineItemCountValidate
    private final List<OrderLineItemCreateRequest> orderLineItems;

    @JsonCreator
    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return Order.of(orderTableId, OrderStatus.COOKING);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}

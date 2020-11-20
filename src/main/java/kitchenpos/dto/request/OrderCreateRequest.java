package kitchenpos.dto.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.Order;

public class OrderCreateRequest {

    @NotNull
    private final Long orderTableId;

    @NotNull
    @NotEmpty
    private final List<@Valid OrderLineItemCreateRequest> orderLineItems;

    @JsonCreator
    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return Order.of(orderTableId, Order.INITIAL_STATUS);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}

package kitchenpos.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

public class OrderCreateRequest {
    @NotNull(message = "테이블이 비어있을수 없습니다.")
    private final Long orderTableId;

    @NotEmpty(message = "주문 항목이 비어있을수 없습니다.")
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity(final OrderTable orderTable) {
        return new Order(orderTable);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}

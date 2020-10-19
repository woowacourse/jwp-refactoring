package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {
    @NotNull(message = "테이블이 비어있을수 없습니다.")
    private final Long orderTableId;

    @NotEmpty(message = "주문 항목이 비어있을수 없습니다.")
    private final List<OrderLineItemRequest> orderLimeItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLimeItems) {
        this.orderTableId = orderTableId;
        this.orderLimeItems = orderLimeItems;
    }

    public Order toEntity() {
        final List<OrderLineItem> orderLineItems = orderLimeItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());

        return new Order(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLimeItems() {
        return orderLimeItems;
    }
}

package kitchenpos.order.application.request;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        validate(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private void validate(final List<OrderLineItemDto> orderLineItemDtos) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}

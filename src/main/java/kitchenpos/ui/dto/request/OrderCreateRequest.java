package kitchenpos.ui.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import org.springframework.util.CollectionUtils;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public void verify() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItemEntities() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::toEntity)
                .collect(Collectors.toList());
    }
}

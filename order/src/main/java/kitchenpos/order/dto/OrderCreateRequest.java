package kitchenpos.order.dto;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        validateOrderLineItem(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItem(final List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 빈 값이거나 빈 컬렉션이 될 수 없습니다.");
        }

    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}

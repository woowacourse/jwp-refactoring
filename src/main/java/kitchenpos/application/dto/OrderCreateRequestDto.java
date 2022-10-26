package kitchenpos.application.dto;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequestDto {

    private Long orderTableId;

    private List<OrderLineItem> orderLineItems;

    public OrderCreateRequestDto(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        validateOrderLineItem(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItem(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어 있을 수 없습니다.");
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}

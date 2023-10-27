package kitchenpos.order.dto;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemCreateRequest {

        private final Long menuId;
        private final long quantity;

        public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}

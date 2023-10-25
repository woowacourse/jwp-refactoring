package kitchenpos.application.dto.request;

import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final String orderStatus;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(
            final Long orderTableId,
            final String orderStatus,
            final List<OrderLineItemRequest> orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemRequest {

        private final Long menuId;
        private final Long quantity;

        public OrderLineItemRequest(final Long menuId, final Long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}

package kitchenpos.order.application.dto;

import java.util.List;

public final class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreate> orderLineItemCreates;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreate> orderLineItemCreates) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreates = orderLineItemCreates;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreate> getOrderLineItemCreates() {
        return orderLineItemCreates;
    }

    public static class OrderLineItemCreate {

        private final Long menuId;
        private final Long quantity;

        public OrderLineItemCreate(final Long menuId, final Long quantity) {
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

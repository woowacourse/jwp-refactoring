package kitchenpos.order.application.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> createOrderLineItems() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::createOrderLineItem)
                .collect(Collectors.toList());
    }

    public static class OrderLineItemRequest {

        private final Long menuId;
        private final int quantity;

        public OrderLineItemRequest(final Long menuId, final int quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public OrderLineItem createOrderLineItem() {
            return new OrderLineItem(menuId, quantity);
        }

        public Long getMenuId() {
            return menuId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}

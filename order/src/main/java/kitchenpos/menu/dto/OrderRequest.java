package kitchenpos.menu.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(final Long orderTableId,
                        final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Map<Long, Long> toMap() {
        return orderLineItems.stream()
            .collect(Collectors.toMap(
                OrderLineItemRequest::getMenuId,
                OrderLineItemRequest::getQuantity
            ));
    }

    public static class OrderLineItemRequest {

        private Long menuId;
        private Long quantity;

        protected OrderLineItemRequest() {
        }

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

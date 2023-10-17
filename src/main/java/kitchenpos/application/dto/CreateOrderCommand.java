package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderCommand {

    public static class OrderLineItemRequest {
        private Long menuId;
        private int quantity;

        public OrderLineItemRequest(final Long menuId, final int quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public int getQuantity() {
            return quantity;
        }

    }

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public CreateOrderCommand(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

}

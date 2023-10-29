package kitchenpos.application;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class CreateOrderCommand {

    public static class OrderLineItemRequest {
        private Long menuId;
        private Long quantity;

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

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public CreateOrderCommand(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

}

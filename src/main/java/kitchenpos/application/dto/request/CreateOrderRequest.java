package kitchenpos.application.dto.request;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItem> orderLineItems;

    private CreateOrderRequest() {
    }

    public CreateOrderRequest(Long orderTableId, List<CreateOrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static CreateOrderRequestBuilder builder() {
        return new CreateOrderRequestBuilder();
    }

    public static final class CreateOrderRequestBuilder {
        private Long orderTableId;
        private List<CreateOrderLineItem> orderLineItems;

        private CreateOrderRequestBuilder() {
        }

        public CreateOrderRequestBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public CreateOrderRequestBuilder orderLineItems(List<CreateOrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public CreateOrderRequest build() {
            return new CreateOrderRequest(orderTableId, orderLineItems);
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<CreateOrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class CreateOrderLineItem {
        private long menuId;
        private long quantity;

        private CreateOrderLineItem() {
        }

        public CreateOrderLineItem(long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}

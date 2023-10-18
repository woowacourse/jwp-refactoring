package kitchenpos.order.application.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemInfo> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemInfo> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemInfo> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemInfo {

        private Long menuId;
        private int quantity;

        public OrderLineItemInfo() {
        }

        public OrderLineItemInfo(Long menuId, int quantity) {
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
}

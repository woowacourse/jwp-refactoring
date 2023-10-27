package kitchenpos.order.application.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineRequest {

        private Long menuId;
        private Integer quantity;

        public OrderLineRequest(final Long menuId, final Integer quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}

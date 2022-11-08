package kitchenpos.ui.dto;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderInnerLineItems> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(final Long orderTableId,
                        final List<OrderInnerLineItems> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderInnerLineItems> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderInnerLineItems {

        private Long menuId;
        private long quantity;

        private OrderInnerLineItems() {
        }

        public OrderInnerLineItems(final Long menuId, final long quantity) {
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

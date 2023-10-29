package kitchenpos.order.application;

import java.util.List;

public class OrderSheet {

    private Long orderTableId;
    private List<OrderSheetItem> orderSheetItems;

    public OrderSheet(final Long orderTableId, final List<OrderSheetItem> orderSheetItems) {
        this.orderTableId = orderTableId;
        this.orderSheetItems = orderSheetItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderSheetItem> getOrderSheetItems() {
        return orderSheetItems;
    }

    public static class OrderSheetItem {

        private Long menuId;
        private Long quantity;

        public OrderSheetItem(final long menuId, final long quantity) {
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

package kitchenpos.application.dto;

public class OrderLineItemRequest {

    public static class Create {
        private Long menuId;
        private long quantity;

        public Create() {
        }

        public Create(Long menuId, long quantity) {
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

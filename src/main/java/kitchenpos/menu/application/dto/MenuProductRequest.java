package kitchenpos.menu.application.dto;

public class MenuProductRequest {

    public static class Create {
        private Long productId;
        private long quantity;

        private Create() {
        }

        public Create(Long productId, long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}

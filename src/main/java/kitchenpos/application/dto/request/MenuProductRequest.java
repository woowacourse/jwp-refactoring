package kitchenpos.application.dto.request;

public class MenuProductRequest {

    private MenuProductRequest() {
    }

    private Long productId;
    private long quantity;

    public static MenuProductRequestBuilder builder() {
        return new MenuProductRequestBuilder();
    }

    public static final class MenuProductRequestBuilder {
        private Long productId;
        private long quantity;

        private MenuProductRequestBuilder() {
        }

        public MenuProductRequestBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductRequestBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductRequest build() {
            MenuProductRequest menuProductRequest = new MenuProductRequest();
            menuProductRequest.productId = this.productId;
            menuProductRequest.quantity = this.quantity;
            return menuProductRequest;
        }
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
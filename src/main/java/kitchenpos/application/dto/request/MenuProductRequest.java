package kitchenpos.application.dto.request;

public class MenuProductRequest {

    private MenuProductRequest() {
    }

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public static MenuProductRequestBuilder builder() {
        return new MenuProductRequestBuilder();
    }

    public static final class MenuProductRequestBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        private MenuProductRequestBuilder() {
        }

        public MenuProductRequestBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductRequestBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
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
            menuProductRequest.seq = this.seq;
            menuProductRequest.menuId = this.menuId;
            menuProductRequest.productId = this.productId;
            menuProductRequest.quantity = this.quantity;
            return menuProductRequest;
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}

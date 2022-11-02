package kitchenpos.support.fixture;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.MenuProductRequest;

public abstract class MenuProductFixture {

    public static MenuProductRequestBuilder menuProductRequestBuilder() {
        return new MenuProductRequestBuilder();
    }

    public static class MenuProductRequestBuilder {

        private Long productId;
        private long quantity;

        public MenuProductRequestBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductRequestBuilder product(Product product) {
            this.productId = product.getId();
            return this;
        }

        public MenuProductRequestBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductRequest build() {
            return new MenuProductRequest(productId, quantity);
        }
    }
}

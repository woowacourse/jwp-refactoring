package kitchenpos.application.support.domain;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Menu menu = null;
        private Product product = ProductTestSupport.builder().build();
        private long quantity = 2;

        public Builder menu(final Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder product(final Product product) {
            this.product = this.product;
            return this;
        }

        public Builder quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(product, quantity);
        }
    }
}

package support.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductBuilder {

    private Menu menu;
    private Product product;
    private long quantity;

    public MenuProductBuilder() {
        this.menu = null;
        this.product = null;
        this.quantity = 0;
    }

    public MenuProductBuilder setMenu(final Menu menu) {
        this.menu = menu;
        return this;
    }

    public MenuProductBuilder setProduct(final Product product) {
        this.product = product;
        return this;
    }

    public MenuProductBuilder setQuantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(menu, product, quantity);
    }
}

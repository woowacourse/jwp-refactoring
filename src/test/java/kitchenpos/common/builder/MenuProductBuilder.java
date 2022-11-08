package kitchenpos.common.builder;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductBuilder {

    private Long menuId;
    private Product product;
    private long quantity;

    public MenuProductBuilder menuId(final Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuProductBuilder product(final Product product) {
        this.product = product;
        return this;
    }

    public MenuProductBuilder quantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(product, quantity);
    }
}

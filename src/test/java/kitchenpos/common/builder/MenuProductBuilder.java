package kitchenpos.common.builder;

import kitchenpos.domain.MenuProduct;

public class MenuProductBuilder {

    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductBuilder menuId(final Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public MenuProductBuilder productId(final Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductBuilder quantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(productId, quantity);
    }
}

package kitchenpos.builder;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductBuilder {

    private Long seq;
    private Menu menu;
    private Long productId;
    private long quantity;

    public MenuProductBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductBuilder menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public MenuProductBuilder productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public MenuProductBuilder quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return new MenuProduct(this.seq, this.menu, this.productId, this.quantity);
    }
}

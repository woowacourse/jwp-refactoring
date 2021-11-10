package kitchenpos.builder;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductBuilder {

    private Long seq;
    private Menu menu;
    private Product product;
    private long quantity;

    public MenuProductBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductBuilder menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public MenuProductBuilder product(Product product) {
        this.product = product;
        return this;
    }

    public MenuProductBuilder quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public MenuProduct build() {
        return  new MenuProduct(this.seq, this.menu, this.product, this.quantity);
    }
}

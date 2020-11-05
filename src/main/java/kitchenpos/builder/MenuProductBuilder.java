package kitchenpos.builder;

import kitchenpos.domain.MenuProduct;

public class MenuProductBuilder {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductBuilder() {
    }

    public MenuProductBuilder(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public MenuProductBuilder menuId(Long menuId) {
        this.menuId = menuId;
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
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}

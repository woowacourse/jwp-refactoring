package kitchenpos.domain;

import kitchenpos.builder.MenuProductBuilder;

public class MenuProduct {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public MenuProductBuilder toBuilder() {
        return new MenuProductBuilder(seq, menuId, productId, quantity);
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

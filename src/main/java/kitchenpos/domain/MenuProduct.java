package kitchenpos.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct (MenuProductBuilder menuProductBuilder) {
        this.seq = menuProductBuilder.seq;
        this.menuId = menuProductBuilder.menuId;
        this.productId = menuProductBuilder.productId;
        this.quantity = menuProductBuilder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductBuilder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder setMenuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductBuilder setProductId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductBuilder setQuantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }
}

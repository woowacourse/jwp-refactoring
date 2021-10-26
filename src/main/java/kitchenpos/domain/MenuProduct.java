package kitchenpos.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct(Builder builder) {
        this.seq = builder.seq;
        this.menuId = builder.menuId;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        private Builder() {
        }

        public Builder of(MenuProduct menuProduct) {
            this.seq = menuProduct.seq;
            this.menuId = menuProduct.menuId;
            this.productId = menuProduct.productId;
            this.quantity = menuProduct.quantity;
            return this;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }

    public Long getSeq() {
        return seq;
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

    public long getQuantity() {
        return quantity;
    }
}

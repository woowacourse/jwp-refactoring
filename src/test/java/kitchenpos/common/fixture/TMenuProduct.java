package kitchenpos.common.fixture;

import kitchenpos.domain.MenuProduct;

public class TMenuProduct {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

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

        public MenuProduct builder() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setSeq(seq);
            menuProduct.setMenuId(menuId);
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(quantity);

            return menuProduct;
        }
    }
}

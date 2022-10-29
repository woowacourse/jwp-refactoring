package kitchenpos.fixtures.domain;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(final Long productId, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static class MenuProductRequestBuilder {

        private Long menuId;
        private Long productId;
        private long quantity = 0;

        public MenuProductRequestBuilder menuId(final Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductRequestBuilder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductRequestBuilder quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(menuId);
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(quantity);

            return menuProduct;
        }
    }
}

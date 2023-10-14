package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static class MENU_PRODUCT {

        public static MenuProduct 후라이드_치킨() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(1L);
            menuProduct.setQuantity(1);
            return menuProduct;
        }
    }
}

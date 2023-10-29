package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 후라이드_강정치킨() {
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();
        Product 상품_강정치킨 = ProductFixture.상품_강정치킨();
        return new MenuProduct(1L, 메뉴_후라이드.getId(), 상품_강정치킨.getId(), 2L);
    }
}

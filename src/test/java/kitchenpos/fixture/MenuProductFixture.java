package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Product savedProduct, long quantity) {
        return new MenuProduct(
                savedProduct.getId(),
                quantity
        );
    }

    public static MenuProduct 존재하지_않는_상품을_가진_메뉴_상품() {
        return new MenuProduct(
                Long.MAX_VALUE,
                0
        );
    }

}

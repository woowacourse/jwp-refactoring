package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품_생성(final Long productId, final Long quantity) {
        return new MenuProduct(productId, quantity);
    }
}

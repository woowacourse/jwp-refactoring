package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long productId, Long quantity) {
        return new MenuProduct(productId, quantity);
    }
}

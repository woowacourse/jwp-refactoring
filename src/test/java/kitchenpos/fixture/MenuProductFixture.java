package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long menuId, Long productId, Long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }
}

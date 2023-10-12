package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct 메뉴상품(Long productId, int quantity) {
        return new MenuProduct(productId, quantity);
    }
}

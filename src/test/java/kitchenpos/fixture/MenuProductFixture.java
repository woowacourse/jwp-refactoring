package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct menuProduct(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }
}

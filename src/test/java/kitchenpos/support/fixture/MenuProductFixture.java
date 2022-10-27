package kitchenpos.support.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Long menuId, Long productId) {
        return new MenuProduct(menuId, productId, 1L);
    }
}

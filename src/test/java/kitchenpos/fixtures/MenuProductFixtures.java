package kitchenpos.fixtures;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    public static MenuProduct create(final Long menuId, final Long productId, long quantity) {
        return new MenuProduct(null, menuId, productId, quantity);
    }
}

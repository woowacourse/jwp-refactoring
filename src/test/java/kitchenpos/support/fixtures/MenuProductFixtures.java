package kitchenpos.support.fixtures;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixtures {

    public static MenuProduct create(final Menu menu, final Long productId, long quantity) {
        return new MenuProduct(null, menu, productId, quantity);
    }
}

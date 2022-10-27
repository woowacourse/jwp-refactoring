package kitchenpos.fixtures;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    public static MenuProduct create(final Menu menu, final Long productId, long quantity) {
        return new MenuProduct(null, menu, productId, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct menuProduct(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }
}

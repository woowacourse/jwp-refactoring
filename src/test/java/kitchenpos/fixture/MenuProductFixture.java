package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    public static MenuProduct menuProduct() {
        return new MenuProduct(0L, 0L, 0L, 0L);
    }
}

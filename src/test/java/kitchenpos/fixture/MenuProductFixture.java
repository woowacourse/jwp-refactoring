package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixture {
    private static final MenuProduct menuProduct = new MenuProduct(0L, 0L, 0L, 0L);

    public static MenuProduct menuProduct() {
        return menuProduct;
    }
}

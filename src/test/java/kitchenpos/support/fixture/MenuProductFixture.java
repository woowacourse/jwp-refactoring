package kitchenpos.support.fixture;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createDefaultWithoutId(final Long productId) {
        return createWithQuantity(productId, 1L);
    }

    public static MenuProduct createWithQuantity(final Long productId, final Long quantity) {
        return new MenuProduct(null, productId, quantity);
    }
}

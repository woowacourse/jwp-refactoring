package kitchenpos.support.fixture;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductFixture {

    public static MenuProduct createDefaultWithoutId(final Long productId, final Long priceValue) {
        return createWithQuantity(productId, 1L, priceValue);
    }

    public static MenuProduct createWithQuantity(final Long productId, final Long quantity, final Long priceValue) {
        return new MenuProduct( productId, quantity, null, new Price(priceValue));
    }
}

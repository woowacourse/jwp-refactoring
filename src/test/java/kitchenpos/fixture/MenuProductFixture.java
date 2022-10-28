package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createDefaultWithoutId(final Product product, final Menu menu) {
        return createWithQuantity(product, menu, 1L);
    }

    public static MenuProduct createWithQuantity(final Product product, final Menu menu, final Long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }
}

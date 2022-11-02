package kitchenpos.support.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createDefaultWithoutId(final Product product, final Menu menu) {
        return createWithQuantity(product, menu, 1L);
    }

    public static MenuProduct createWithQuantity(final Product product, final Menu menu, final Long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        return new MenuProduct(product.getId(), menu, quantity);
    }
}

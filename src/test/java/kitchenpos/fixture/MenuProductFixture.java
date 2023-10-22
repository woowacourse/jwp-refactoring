package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct menuProduct(Product product, long quantity, Menu menu) {
        return new MenuProduct(menu, product, quantity);
    }
}

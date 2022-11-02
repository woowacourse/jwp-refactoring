package kitchenpos.support.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Menu menu, Product product) {
        return new MenuProduct(menu, product, 1L);
    }
}

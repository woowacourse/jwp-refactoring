package kitchenpos.support.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct createMenuProduct(Menu menu, Product product) {
        return new MenuProduct(menu, product, 1L);
    }
}

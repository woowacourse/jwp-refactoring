package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct menuProduct(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }
}

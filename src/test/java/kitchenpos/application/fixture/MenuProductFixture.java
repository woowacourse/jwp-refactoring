package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public abstract class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct menuProduct(final Product product, final long price) {
        return new MenuProduct(product, price);
    }
}

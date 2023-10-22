package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixtures {

    public static MenuProduct create(Product product, long quantity) {
        return new MenuProduct(product.getId(), quantity);
    }

}

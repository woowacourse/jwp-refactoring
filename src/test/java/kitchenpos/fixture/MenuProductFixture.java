package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {
    
    public static MenuProduct MENU_PRODUCT(Product product) {
        MenuProduct menuProduct = new MenuProduct(
                null,
                product,
                1L
        );
        return menuProduct;
    }
}

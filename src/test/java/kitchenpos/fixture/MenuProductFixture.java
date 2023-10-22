package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;

public class MenuProductFixture {
    
    public static MenuProduct MENU_PRODUCT(Product product) {
        MenuProduct menuProduct = new MenuProduct(
                null,
                product,
                new Quantity(1L)
        );
        return menuProduct;
    }
}

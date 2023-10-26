package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴상품(Product product, int quantity) {
        return new MenuProduct(product, quantity);
    }
}

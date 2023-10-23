package kitchenpos.fixture;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }
}

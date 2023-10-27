package kitchenpos.test.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Product product, long quantity) {
        return new MenuProduct(
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity
        );
    }
}

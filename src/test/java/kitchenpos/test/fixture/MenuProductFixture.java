package kitchenpos.test.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product, quantity);
    }
}

package kitchenpos.fixture;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuProduct;
import kitchenpos.product.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(final Long seq, final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}

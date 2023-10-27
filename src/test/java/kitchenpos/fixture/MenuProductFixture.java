package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(final Long seq, final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}

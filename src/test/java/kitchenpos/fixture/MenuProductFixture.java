package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(final Long seq, final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }
}

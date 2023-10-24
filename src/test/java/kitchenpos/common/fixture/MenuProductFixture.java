package kitchenpos.common.fixture;

import static kitchenpos.common.fixture.ProductFixture.상품;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.vo.Quantity;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    public static MenuProduct 메뉴_상품(Long seq, Product product) {
        return new MenuProduct(seq, product, Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품(Product product) {
        return new MenuProduct(product, Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품() {
        return new MenuProduct(상품(), Quantity.valueOf(1L));
    }

    public static MenuProduct 메뉴_상품(Product product, long quantity) {
        return new MenuProduct(product, Quantity.valueOf(quantity));
    }
}

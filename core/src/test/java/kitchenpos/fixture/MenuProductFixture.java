package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    public static MenuProduct 메뉴_상품_생성(final Product product) {
        return new MenuProduct(product.getId(), product.getPrice(), DEFAULT_QUANTITY);
    }

    public static List<MenuProduct> 메뉴_상품들_생성(final List<Product> products) {
        final List<MenuProduct> 메뉴_상품들 = new ArrayList<>();

        for (Product product : products) {
            메뉴_상품들.add(메뉴_상품_생성(product));
        }

        return 메뉴_상품들;
    }
}

package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuProductFixture {

    private static final long DEFAULT_QUANTITY = 1L;

    public static MenuProduct 메뉴_상품_생성(final Product product, final Menu menu) {
        final MenuProduct 메뉴_상품 = new MenuProduct(product, DEFAULT_QUANTITY);
        메뉴_상품.updateMenu(menu);

        return 메뉴_상품;
    }

    public static List<MenuProduct> 메뉴_상품들_생성(final List<Product> products, final Menu menu) {
        final List<MenuProduct> 메뉴_상품들 = new ArrayList<>();

        for (Product product : products) {
            메뉴_상품들.add(메뉴_상품_생성(product, menu));
        }

        return 메뉴_상품들;
    }
}

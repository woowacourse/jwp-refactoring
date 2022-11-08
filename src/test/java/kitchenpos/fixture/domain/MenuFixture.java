package kitchenpos.fixture.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuFixture {

    // 해당 픽스쳐를 이용해 만든 메뉴의 각 상품 개수는 모두 1개로 설정된다.
    private static final int QUANTITY = 1;

    public static Menu createMenu(final String name, final long price, final MenuGroup menuGroup,
                                  final Product... products) {
        return new Menu(name, BigDecimal.valueOf(price), menuGroup.getId(), createMenuProducts(products));
    }

    private static List<MenuProduct> createMenuProducts(final Product[] products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        long seq = 1L;
        for (final Product product : products) {
            final MenuProduct menuProduct = new MenuProduct(seq++, null, product.getId(), product.getPrice(), QUANTITY);
            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }
}

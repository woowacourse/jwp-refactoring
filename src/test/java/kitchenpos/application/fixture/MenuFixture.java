package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    // 해당 픽스쳐를 이용해 만든 메뉴의 각 상품 개수는 모두 1개로 설정된다.
    private static final int QUANTITY = 1;

    public static Menu createMenu(final String name, final int price, final MenuGroup menuGroup,
                                  final Product... products) {
        final Menu menu = new Menu();
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        setMenuProducts(menu, products);

        return menu;
    }

    private static void setMenuProducts(final Menu menu, final Product[] products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final Product product : products) {
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(QUANTITY);

            menuProducts.add(menuProduct);
        }

        menu.setMenuProducts(menuProducts);
    }
}

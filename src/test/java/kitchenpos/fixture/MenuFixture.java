package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu createDefaultWithoutId(final MenuGroup menuGroup, final Product... products) {
        final Menu menu = new Menu();
        menu.setName("name");
        menu.setPrice(new BigDecimal(2000L));
        menu.setMenuGroupId(menuGroup.getId());
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(products);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createWithPrice(final MenuGroup menuGroup, final Long price, final Product... products) {
        final Menu menu = createDefaultWithoutId(menuGroup, products);
        if (price == null) {
            menu.setPrice(null);
            return menu;
        }
        menu.setPrice(BigDecimal.valueOf(price));
        return menu;
    }

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final Product... products) {
        final Menu menu = new Menu();
        return Arrays.stream(products)
                .map(product -> MenuProductFixture.createDefaultWithoutId(product, menu))
                .collect(Collectors.toList());
    }
}

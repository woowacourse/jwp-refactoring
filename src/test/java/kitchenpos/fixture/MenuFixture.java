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

    public static Menu createDefaultWithoutId(final Long menuGroupId, final Long... productIds) {
        final Menu menu = new Menu();
        menu.setName("name");
        menu.setPrice(new BigDecimal(2000L));
        menu.setMenuGroupId(menuGroupId);
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(productIds);
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

    public static Menu createWithPrice(final Long menuGroupId, final Long price, final Long... productIds) {
        final Menu menu = createDefaultWithoutId(menuGroupId, productIds);
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

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final Long... productIds) {
        return Arrays.stream(productIds)
                .map(product -> MenuProductFixture.createDefaultWithoutId(product, null))
                .collect(Collectors.toList());
    }
}

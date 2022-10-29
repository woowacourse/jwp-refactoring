package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu createDefaultWithoutId(final Long menuGroupId, final Product... product) {
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(product);
        return new Menu("name", BigDecimal.valueOf(2000), menuGroupId, menuProducts);
    }

    public static Menu createWithPrice(final Long menuGroupId, final Long price, final Product... product) {
        return createWithIdAndPrice( menuGroupId, price, product);
    }

    private static Menu createWithIdAndPrice(final Long menuGroupId, final Long price,
                                            final Product... product) {
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(product);
        if (price == null) {
            return new Menu("name", null, menuGroupId, menuProducts);
        }
        return new Menu("name", BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final Product... product) {
        return Arrays.stream(product)
                .map(it -> MenuProductFixture.createDefaultWithoutId(it, null))
                .collect(Collectors.toList());
    }
}

package kitchenpos.support.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;

public class MenuFixture {

    public static Menu createWithPrice(final Long menuGroupId, final Long price, final Long... products) {
        return createWithIdAndPrice( menuGroupId, price, products);
    }

    private static Menu createWithIdAndPrice(final Long menuGroupId, final Long price,
                                            final Long... productIds) {
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(productIds);
        if (price == null) {
            return new Menu("name", new Price(price), menuGroupId, menuProducts);
        }
        final BigDecimal priceValue = BigDecimal.valueOf(price);
        return new Menu("name", new Price(priceValue), menuGroupId, menuProducts);
    }

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final Long... productId) {
        return Arrays.stream(productId)
                .map(MenuProductFixture::createDefaultWithoutId)
                .collect(Collectors.toList());
    }
}

package kitchenpos.support.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.support.fixture.exception.FixtureException;

public class MenuFixture {

    public static Menu createWithPrice(final Long menuGroupId, final Long price, final List<Long> productIds, final List<Long> menuProductPrices) {
        return createWithIdAndPrice( menuGroupId, price, productIds, menuProductPrices);
    }

    private static Menu createWithIdAndPrice(final Long menuGroupId, final Long price,
                                            final List<Long> productIds, final List<Long> menuProductPrices) {
        final List<MenuProduct> menuProducts = convertToEmptyMenuIdMenuProduct(productIds, menuProductPrices);
        if (price == null) {
            return new Menu("name", new Price(price), menuGroupId, menuProducts);
        }
        final BigDecimal priceValue = BigDecimal.valueOf(price);
        return new Menu("name", new Price(priceValue), menuGroupId, menuProducts);
    }

    private static List<MenuProduct> convertToEmptyMenuIdMenuProduct(final List<Long> productIds, final List<Long> menuProductPrices) {
        validateProductIdsCountSameAsMenuProductPrices(productIds, menuProductPrices);
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            final Long productId = productIds.get(i);
            final Long priceValue = menuProductPrices.get(i);
            final MenuProduct menuProduct = MenuProductFixture.createDefaultWithoutId(productId, priceValue);
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private static void validateProductIdsCountSameAsMenuProductPrices(final List<Long> productIds, final List<Long> menuProductPrices) {
        if (productIds.size() != menuProductPrices.size()) {
            throw new FixtureException("MenuProduct 생성 실패!");
        }
    }
}

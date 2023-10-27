package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Price;

public class MenuProducts {

    private List<MenuProduct> menuProducts;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public boolean isBiggerThanTotalPrice(Price price) {
        final List<Price> pricesPerMenuProduct = menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .collect(Collectors.toList());

        Price totalPrice = new Price(BigDecimal.ZERO);
        for (Price pricePerMenuProduct : pricesPerMenuProduct) {
            totalPrice = totalPrice.add(pricePerMenuProduct);
        }

        return price.isBiggerThan(totalPrice);
    }
}

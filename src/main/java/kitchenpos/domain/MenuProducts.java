package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.InvalidMenuPriceException;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(Price menuPrice) {
        Prices prices = getMenuTotalPrices();
        Price totalPrice = prices.sumAll();

        if (menuPrice.isBiggerThan(totalPrice)) {
            throw new InvalidMenuPriceException(
                String.format("메뉴의 가격 %s이 상품 가격의 합 %s보다 큽니다.", menuPrice.getValue(), totalPrice.getValue())
            );
        }
    }

    private Prices getMenuTotalPrices() {
        List<Price> prices = menuProducts.stream()
            .map(MenuProduct::productTotalPrice)
            .collect(Collectors.toList());

        return new Prices(prices);
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }
}

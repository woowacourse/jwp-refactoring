package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.exception.InvalidMenuPriceException;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(MenuPrice menuPrice) {
        MenuPrices menuPrices = getMenuTotalPrices();
        MenuPrice totalMenuPrice = menuPrices.sumAll();

        if (menuPrice.isBiggerThan(totalMenuPrice)) {
            throw new InvalidMenuPriceException(
                String.format("메뉴의 가격 %s이 상품 가격의 합 %s보다 큽니다.", menuPrice.getValue(), totalMenuPrice.getValue())
            );
        }
    }

    private MenuPrices getMenuTotalPrices() {
        List<MenuPrice> menuPrices = menuProducts.stream()
            .map(MenuProduct::productTotalPrice)
            .collect(Collectors.toList());

        return new MenuPrices(menuPrices);
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }
}

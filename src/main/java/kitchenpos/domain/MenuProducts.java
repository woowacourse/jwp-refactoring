package kitchenpos.domain;

import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(Price menuPrice) {
        Price productsPrice = Price.of(menuProducts);

        if (menuPrice.isBiggerThan(productsPrice)) {
            throw new InvalidMenuPriceException(
                String.format("메뉴의 가격 %s이 상품 가격의 합 %s보다 큽니다.", menuPrice.getValue(), productsPrice.getValue())
            );
        }
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }
}

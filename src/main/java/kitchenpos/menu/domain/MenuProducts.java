package kitchenpos.menu.domain;

import kitchenpos.generic.Price;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        this.menuProducts = menuProducts;
        this.addMenu(menu);
        validate(menu.getPrice());
    }

    private void validate(BigDecimal requestPrice) {
        Price sum = calculateSum();
        if (sum.isLessThan(requestPrice)) {
            throw new IllegalArgumentException(String.format("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", sum.longValue(), requestPrice.longValue()));
        }
    }

    private Price calculateSum() {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateSum)
                .reduce(BigDecimal.valueOf(0L), BigDecimal::add);
        return new Price(sum);
    }

    private void addMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.placeMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}

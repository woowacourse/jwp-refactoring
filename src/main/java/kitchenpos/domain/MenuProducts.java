package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        this.menuProducts = menuProducts;
        this.addMenu(menu);
        validateMenuPrice(menu.getPrice());
    }

    private void validateMenuPrice(BigDecimal price) {
        BigDecimal sum = menuProducts.stream()
            .map(MenuProduct::calculateSumOfMenuProduct)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sum.compareTo(price) < 0) {
            throw new IllegalArgumentException("상품 금액의 합이 메뉴의 가격보다 작습니다.");
        }
    }

    private void addMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.arrangeMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}

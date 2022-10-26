package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(Price price, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateAmount(price);
    }

    private void validateAmount(Price price) {
        if (price.isExpensive(sum())) {
            throw new IllegalArgumentException("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
        }
    }

    public void changeAllMenuId(Long menuId) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.placeMenuId(menuId);
        }
    }

    public BigDecimal sum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice());
        }
        return sum;
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(Price price, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateDiscount(price);
    }
    private void validateDiscount(Price price) {
        if (price.isExpensive(getSum())) {
            throw new IllegalArgumentException("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
        }
    }

    public void updateMenuId(Long menuId) {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenuId(menuId);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public BigDecimal getSum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice());
        }
        return sum;
    }
}

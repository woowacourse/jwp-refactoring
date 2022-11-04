package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.Price;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public MenuProducts(Long menuId, Price price, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateAmount(price);
        changeAllMenuId(menuId);
    }

    public MenuProducts(Price price, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateAmount(price);
    }

    public MenuProducts(Long menuId, List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        changeAllMenuId(menuId);
    }

    public MenuProducts() {
        this.menuProducts = new ArrayList<>();
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

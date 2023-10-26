package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void setMenu(Menu menu) {
        menuProducts.forEach(menu::addMenuProduct);
    }

    public BigDecimal calculateSum() {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

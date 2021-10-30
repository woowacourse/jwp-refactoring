package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal sum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    public void setMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(menu);
        }
    }

    public List<MenuProduct> toList() {
        return menuProducts;
    }
}

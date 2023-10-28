package kitchenpos.domain.menuproduct;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {
    private List<MenuProduct> menuProducts;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateMenuProductsPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final BigDecimal menuProductPrice = menuProduct.calculatePrice();
            sum = sum.add(menuProductPrice);
        }
        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

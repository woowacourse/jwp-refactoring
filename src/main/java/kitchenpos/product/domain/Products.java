package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;

public class Products {

    private final List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public BigDecimal calculateAmount(final List<MenuProduct> menuProducts) {
        BigDecimal amount = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            amount = amount.add(calculateTotalPrice(menuProduct));
        }
        return amount;
    }

    private BigDecimal calculateTotalPrice(final MenuProduct menuProduct) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            sum = sum.add(menuProduct.calculateAmount(product));
        }
        return sum;
    }
}

package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Price calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return new Price(sum);
    }

    public void add(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getAll() {
        return Collections.unmodifiableList(menuProducts);
    }
}

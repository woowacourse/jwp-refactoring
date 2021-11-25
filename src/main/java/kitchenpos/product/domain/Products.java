package kitchenpos.product.domain;

import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private final List<Product> values;

    public Products(List<Product> values) {
        this.values = values;
    }

    public Product findById(Long id) {
        return values.stream()
                .filter(value -> id.equals(value.getId()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public BigDecimal sumTotalPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = findById(menuProduct.getProductId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    public List<Product> getValues() {
        return values;
    }
}

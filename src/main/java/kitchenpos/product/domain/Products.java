package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public BigDecimal calculateAmount(final List<Quantity> quantities) {
        BigDecimal amount = BigDecimal.ZERO;
        for (final Quantity quantity : quantities) {
            amount = amount.add(calculateTotalPrice(quantity));
        }
        return amount;
    }

    private BigDecimal calculateTotalPrice(final Quantity quantity) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            sum = sum.add(product.calculateAmount(quantity));
        }
        return sum;
    }
}

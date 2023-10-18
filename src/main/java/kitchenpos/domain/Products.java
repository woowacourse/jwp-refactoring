package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public BigDecimal calculateSum(final List<Integer> counts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int index = 0; index < products.size(); index++) {
            sum = sum.add(products.get(index).getPrice()
                    .multiply(BigDecimal.valueOf(counts.get(index))));
        }
        return sum;
    }
}

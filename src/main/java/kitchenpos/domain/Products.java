package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Map;

public class Products {

    private final Map<Product, Long> productsAndQuantities;

    public Products(Map<Product, Long> productsAndQuantities) {
        this.productsAndQuantities = productsAndQuantities;
    }

    public void validate(Price menuPrice) {
        Price sum = Price.of(BigDecimal.ZERO);

        for (Product product : productsAndQuantities.keySet()) {
            Price price = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(productsAndQuantities.get(product));
            sum = Price.of(sum.add(price.multiply(quantity)));
        }

        if (menuPrice.getPrice()
            .compareTo(sum.getPrice()) > 0) {
            throw new IllegalArgumentException();
        }
    }
}

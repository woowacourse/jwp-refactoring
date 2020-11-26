package kitchenpos.product.domain;

import java.util.List;
import java.util.Map;

import kitchenpos.price.domain.Price;

public class Products {
    private final List<Product> products;

    public Products(final List<Product> products) {
        validate(products);
        this.products = products;
    }

    private void validate(final List<Product> products) {
        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Price calculateTotalPrice(final Map<Long, Long> menuProductQuantity) {
        return products.stream()
            .map(it -> it.calculatePrice(menuProductQuantity.get(it.getId())))
            .reduce(Price.ZERO, Price::add);
    }
}

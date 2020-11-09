package kitchenpos.domain;

import java.util.List;

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

    public Price sum() {
        return products.stream()
            .map(Product::getPrice)
            .reduce(Price.ZERO, Price::add);
    }
}

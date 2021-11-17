package kitchenpos.domain;

import java.util.List;

public class Products {

    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public Product findById(Long id) {
        return products.stream()
            .filter(product -> product.isId(id))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}

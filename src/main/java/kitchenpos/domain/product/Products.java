package kitchenpos.domain.product;

import java.util.List;

public class Products {

    private List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    public Product findById(final Long productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductResponses {
    private final List<ProductResponse> products;

    public ProductResponses(List<Product> products) {
        this.products = products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

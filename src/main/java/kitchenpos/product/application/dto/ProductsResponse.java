package kitchenpos.product.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductsResponse {

    private final List<ProductResponse> products;

    private ProductsResponse(final List<ProductResponse> products) {
        this.products = products;
    }

    public static ProductsResponse from(final List<Product> products) {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return new ProductsResponse(productResponses);
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

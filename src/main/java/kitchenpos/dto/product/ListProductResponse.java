package kitchenpos.dto.product;

import kitchenpos.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ListProductResponse {
    private final List<ProductResponse> products;

    private ListProductResponse(final List<ProductResponse> products) {
        this.products = products;
    }

    public static ListProductResponse from(final List<Product> products) {
        return new ListProductResponse(products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList()));
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

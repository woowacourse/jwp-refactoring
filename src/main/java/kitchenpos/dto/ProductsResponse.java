package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductsResponse {

    private List<ProductResponse> products;

    private ProductsResponse(List<ProductResponse> products) {
        this.products = products;
    }

    public static ProductsResponse from(List<Product> savedProducts) {
        return new ProductsResponse(savedProducts.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList()));
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

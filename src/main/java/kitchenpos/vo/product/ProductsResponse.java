package kitchenpos.vo.product;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductsResponse {
    List<ProductResponse> products;

    private ProductsResponse(List<ProductResponse> products) {
        this.products = products;
    }

    public static ProductsResponse of(List<Product> products) {
        List<ProductResponse> productsResponse = products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        return new ProductsResponse(productsResponse);
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

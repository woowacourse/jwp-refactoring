package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductsResponse {

    private final List<ProductResponse> productResponses;

    public ProductsResponse(final List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }

    public static ProductsResponse from(final List<Product> products) {
        final List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        return new ProductsResponse(productResponses);
    }
}

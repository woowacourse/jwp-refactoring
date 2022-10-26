package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductsResponse {

    private List<ProductResponse> productResponses;

    private ProductsResponse() {
    }

    private ProductsResponse(final List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public static ProductsResponse of(List<Product> products) {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        return new ProductsResponse(productResponses);
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}

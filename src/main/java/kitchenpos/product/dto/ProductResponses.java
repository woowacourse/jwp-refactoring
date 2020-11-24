package kitchenpos.product.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.product.domain.Product;

public class ProductResponses {

    private final List<ProductResponse> productResponses;

    private ProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public static ProductResponses from(List<Product> products) {
        return products.stream()
            .map(ProductResponse::from)
            .collect(collectingAndThen(toList(), ProductResponses::new));
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}

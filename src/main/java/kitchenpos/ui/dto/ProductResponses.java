package kitchenpos.ui.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Product;

public class ProductResponses {

    private List<ProductResponse> productResponses;

    protected ProductResponses() {
    }

    private ProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public static ProductResponses from(List<Product> products) {

        return new ProductResponses(products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList())
        );
    }

    public List<ProductResponse> getProductResponses() {
        return Collections.unmodifiableList(productResponses);
    }
}

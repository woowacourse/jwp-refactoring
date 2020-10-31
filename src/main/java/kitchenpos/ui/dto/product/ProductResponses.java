package kitchenpos.ui.dto.product;

import kitchenpos.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponses {

    private List<ProductResponse> productResponses;

    private ProductResponses(final List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public static ProductResponses from(final List<Product> products) {
        return new ProductResponses(products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList()));
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}

package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import lombok.Getter;

@Getter
public class ProductResponses {
    private final List<ProductResponse> products;

    public ProductResponses(List<Product> products) {
        this.products = products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}

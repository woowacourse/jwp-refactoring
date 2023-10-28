package kitchenpos.product.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toProductResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().getValue()
        );
    }

    public static List<ProductResponse> toProductResponses(final List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}

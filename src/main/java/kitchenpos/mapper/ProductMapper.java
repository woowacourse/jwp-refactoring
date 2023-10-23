package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductResponse;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toProductResponse(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().longValue()
        );
    }

    public static List<ProductResponse> toProductResponses(final List<Product> products) {
        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice().longValue()
                ))
                .collect(Collectors.toList());
    }
}

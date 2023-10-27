package kitchenpos.application.mapper;

import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.product.Product;

public class ProductMapper {

    public static ProductResponse mapToProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue());
    }
}

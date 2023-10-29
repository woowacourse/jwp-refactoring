package kitchenpos.product.application.mapper;

import kitchenpos.product.domain.Product;
import kitchenpos.product.application.dto.response.ProductResponse;

public class ProductMapper {

    public static ProductResponse mapToProductResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue());
    }
}

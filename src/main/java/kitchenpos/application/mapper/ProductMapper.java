package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.domain.Product;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product mapToProduct(final ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.getName(), productCreateRequest.getPrice());
    }
}

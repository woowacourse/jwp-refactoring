package kitchenpos.product.application.mapper;

import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product mapToProduct(final ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.getName(), ProductPrice.of(productCreateRequest.getPrice()));
    }

    public static ProductResponse mapToResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue());
    }
}

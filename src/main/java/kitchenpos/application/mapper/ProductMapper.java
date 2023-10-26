package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product mapToProduct(final ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.getName(), Price.of(productCreateRequest.getPrice()));
    }

    public static ProductResponse mapToResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue());
    }
}

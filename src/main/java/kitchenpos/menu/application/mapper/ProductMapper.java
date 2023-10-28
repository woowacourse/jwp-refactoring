package kitchenpos.menu.application.mapper;

import kitchenpos.menu.application.dto.request.ProductCreateRequest;
import kitchenpos.menu.application.dto.response.ProductResponse;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

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

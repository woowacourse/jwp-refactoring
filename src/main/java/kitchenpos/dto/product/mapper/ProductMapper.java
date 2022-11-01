package kitchenpos.dto.product.mapper;

import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.request.ProductCreateRequest;

public interface ProductMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);
}

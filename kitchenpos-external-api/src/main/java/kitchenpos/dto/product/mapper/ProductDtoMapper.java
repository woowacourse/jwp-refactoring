package kitchenpos.dto.product.mapper;

import java.util.List;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.response.ProductResponse;

public interface ProductDtoMapper {

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponses(List<Product> product);
}

package kitchenpos.dto.product.mapper;

import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.request.ProductCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toProduct(final ProductCreateRequest productCreateRequest) {
        return new Product(productCreateRequest.getName(), productCreateRequest.getPrice());
    }
}

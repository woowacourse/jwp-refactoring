package kitchenpos.domain.product;

import kitchenpos.dto.request.ProductCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private ProductMapper() {
    }

    public Product toProduct(final ProductCreateRequest request) {
        return new Product(request.getName(), request.getPrice());
    }
}

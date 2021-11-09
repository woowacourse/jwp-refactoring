package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product mapFrom(ProductRequest productRequest) {
        return new Product(
                productRequest.getName(),
                productRequest.getPrice()
        );
    }
}

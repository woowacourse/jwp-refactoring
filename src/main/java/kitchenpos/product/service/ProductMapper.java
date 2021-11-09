package kitchenpos.product.service;

import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class
ProductMapper {
    public Product mapFrom(ProductRequest productRequest) {
        return new Product(
                productRequest.getName(),
                productRequest.getPrice()
        );
    }
}

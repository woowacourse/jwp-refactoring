package kitchenpos.product.service;

import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto productDto) {
        return new Product.Builder()
            .name(productDto.getName())
            .price(productDto.getPrice())
            .build();
    }
}

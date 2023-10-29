package kitchenpos.product.service;

import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toEntity(ProductDto productDto) {
        return new Product.Builder()
            .id(productDto.getId())
            .name(productDto.getName())
            .price(productDto.getPrice())
            .build();
    }
}

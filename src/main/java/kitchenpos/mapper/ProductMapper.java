package kitchenpos.mapper;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
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

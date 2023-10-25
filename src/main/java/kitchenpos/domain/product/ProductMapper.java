package kitchenpos.domain.product;

import kitchenpos.dto.request.CreateProductRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    private ProductMapper() {
    }

    public Product toProduct(final CreateProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(new BigDecimal(request.getPrice()))
                .build();
    }
}

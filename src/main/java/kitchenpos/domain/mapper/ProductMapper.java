package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toProduct(final CreateProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(new BigDecimal(request.getPrice()))
                .build();
    }
}

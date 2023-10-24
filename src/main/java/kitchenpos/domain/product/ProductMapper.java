package kitchenpos.domain.product;

import kitchenpos.dto.request.CreateProductRequest;

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

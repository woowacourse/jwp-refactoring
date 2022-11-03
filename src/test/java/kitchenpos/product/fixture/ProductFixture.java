package kitchenpos.product.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    public static Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .build();
    }

    public static Product createProduct(String name) {
        return Product.builder()
                .name(name)
                .build();
    }

    public static Product createProduct(String name, Long price) {
        return Product.builder()
                .name(name)
                .price(BigDecimal.valueOf(price))
                .build();
    }
}

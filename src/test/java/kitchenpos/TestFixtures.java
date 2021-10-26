package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class TestFixtures {

    public static Product createProduct(){
        return Product.builder()
                .id(1L)
                .name("상품이름")
                .price(BigDecimal.TEN)
                .build();
    }
}

package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_강정치킨() {
        return new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
    }
}

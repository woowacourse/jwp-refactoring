package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_생성() {
        return new Product("상품", BigDecimal.valueOf(10_000));
    }
}

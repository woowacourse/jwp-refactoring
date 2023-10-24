package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static final String 상품명 = "상품";

    public static Product 상품_생성() {
        return new Product(상품명, BigDecimal.valueOf(10_000));
    }

    public static List<Product> 상품들_생성(final int count) {
        final List<Product> 상품들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            상품들.add(상품_생성());
        }

        return 상품들;
    }
}

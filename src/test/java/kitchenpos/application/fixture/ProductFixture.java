package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 짜장면 = new Product(1L, "짜장면", BigDecimal.valueOf(8_000));
    public static Product 짬뽕 = new Product(2L, "짬뽕", BigDecimal.valueOf(8_000));
    public static Product 탕수육 = new Product(3L, "탕수육", BigDecimal.valueOf(10_000));
}

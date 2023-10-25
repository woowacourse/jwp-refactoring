package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.vo.Money;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품() {
        return new Product("productName", Money.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)));
    }

    public static Product 상품(Long productId) {
        return new Product(productId, "productName",
                Money.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP)));
    }

    public static Product 상품(BigDecimal price) {
        return new Product("productName", Money.valueOf(price));
    }

    public static Product 상품(String productName, BigDecimal price) {
        return new Product(productName, Money.valueOf(price));
    }
}

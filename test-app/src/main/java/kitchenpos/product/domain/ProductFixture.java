package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.product.vo.Price;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    private static final Price PRICE = Price.valueOf(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP));

    public static Product 상품() {
        return new Product("productName", PRICE);
    }

    public static Product 상품(Long productId) {
        return new Product(productId, "productName", PRICE);
    }

    public static Product 상품(BigDecimal price) {
        return new Product("productName", Price.valueOf(price));
    }
}

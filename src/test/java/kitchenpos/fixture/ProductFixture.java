package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public final class ProductFixture {

    public static Product 상품_생성() {
        final Product product = new Product();

        product.setPrice(BigDecimal.TEN);
        product.setName("프로덕트");

        return product;
    }

    public static Product 상품_생성(final BigDecimal price) {
        final Product product = new Product();

        product.setPrice(price);
        product.setName("프로덕트");

        return product;
    }

    private ProductFixture() {
    }
}

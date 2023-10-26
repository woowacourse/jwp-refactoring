package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProductFixture {

    public static final Product 상품_1000 =  Product.of("상품명", BigDecimal.valueOf(1_000L));

    public static Product of(final String name, final Long price) {
        return Product.of(name, BigDecimal.valueOf(price));
    }
}

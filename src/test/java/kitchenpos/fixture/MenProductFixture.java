package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.상품_1000;

import java.math.BigDecimal;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MenProductFixture {

    public static final MenuProduct 메뉴_상품_1000원_2개 = new MenuProduct(null, 상품_1000, 2L);

    public static MenuProduct of(final Long productPrice, final Long quantity) {
        return new MenuProduct(null,
                Product.of("상품명", BigDecimal.valueOf(productPrice)),
                quantity);
    }
}

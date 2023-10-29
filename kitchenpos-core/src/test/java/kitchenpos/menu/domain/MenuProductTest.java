package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 메뉴_상품_생성() {
        final Product product = Product.of("후라이드", BigDecimal.valueOf(10_000L));

        assertDoesNotThrow(
                () -> new MenuProduct(product.getId(), 10L)
        );
    }
}

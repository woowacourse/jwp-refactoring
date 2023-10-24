package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 메뉴_상품_생성() {
        final Menu menu = Menu.of("치킨", BigDecimal.valueOf(10_000L), null);
        final Product product = Product.of("후라이드", BigDecimal.valueOf(10_000L));

        assertDoesNotThrow(
                () -> new MenuProduct(menu, product, 10L)
        );
    }
}

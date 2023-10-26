package kitchenpos.menu.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 메뉴_상품_생성() {
        final MenuProduct menuProduct = new MenuProduct(Product.of("치킨", BigDecimal.valueOf(10_000L)), 1L);
        final Menu menu = Menu.of("치킨", BigDecimal.valueOf(10_000L), null, List.of(menuProduct));
        final Product product = Product.of("후라이드", BigDecimal.valueOf(10_000L));

        assertDoesNotThrow(
                () -> new MenuProduct(product, 10L)
        );
    }
}

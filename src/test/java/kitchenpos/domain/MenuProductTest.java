package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuProductTest {

    @Test
    void 상품의_가격과_개수를_통해_메뉴_상품_가격을_구할_수_있다() {
        // given
        Product product = new Product("name", BigDecimal.valueOf(1000L));
        MenuProduct menuProduct = new MenuProduct(null, product, 2L);

        // when
        BigDecimal price = menuProduct.calculatePrice();

        // then
        assertThat(price).isEqualTo(BigDecimal.valueOf(2000L));
    }
}

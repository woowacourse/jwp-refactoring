package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class MenuProductTest {

    @Nested
    class calculatePrice_성공_테스트 {

        @Test
        void 가격을_계산할_수_있다() {
            // given
            final var product = new Product("상품", BigDecimal.valueOf(1000L));
            final var menuProduct = new MenuProduct(product, 5L);

            final var expected = Price.from(BigDecimal.valueOf(1000L * 5L));

            // when
            final var actual = menuProduct.calculatePrice();

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}

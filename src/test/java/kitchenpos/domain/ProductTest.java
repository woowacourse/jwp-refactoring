package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품_생성() {
        assertDoesNotThrow(
                () -> Product.of("사과", BigDecimal.valueOf(1_000L))
        );
    }

    @ParameterizedTest
    @NullSource
    void 상품_생성_시_가격이_NULL이면_예외_발생(final BigDecimal price) {
        assertThatThrownBy(
                () -> Product.of("사과", price)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L, -1000L})
    void 상품_생성_시_가격이_음수이면_예외_발생(final Long price) {
        assertThatThrownBy(
                () -> Product.of("사과", BigDecimal.valueOf(price))
        );
    }

    @Test
    void 수량과_가격을_곱한_값_계산() {
        final Product product = Product.of("사과", BigDecimal.valueOf(1_000L));

        final Price result = product.multiplyPrice(10L);

        assertThat(result.getValue().longValue()).isEqualTo(1_000 * 10L);
    }

    @ParameterizedTest
    @NullSource
    void 수량과_가격을_곱한_값_계산_시_수량이_NULL이면_예외_발생(final Long quantity) {
        final Product product = Product.of("사과", BigDecimal.valueOf(1_000L));

        assertThatThrownBy(
                () -> product.multiplyPrice(quantity)
        );
    }
}

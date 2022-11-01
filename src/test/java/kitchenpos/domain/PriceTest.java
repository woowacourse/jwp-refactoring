package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(longs = {-1_000L, -500L, -10L, -1L})
    void 가격이_음수라면_예외가_발생한다(final long value) {
        // given
        final BigDecimal rawPrice = BigDecimal.valueOf(value);

        // when, then
        assertThatThrownBy(() -> new Price(rawPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_null이라면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MoneyTest {

    @Nested
    class 돈을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            // given
            BigDecimal price = BigDecimal.ONE;

            // expect
            assertThatNoException().isThrownBy(() -> Money.valueOf(price));
        }

        @Test
        void 가격이_0_미만이면_예외를_던진다() {
            // given
            BigDecimal price = BigDecimal.valueOf(-1);

            // expect
            assertThatThrownBy(() -> Money.valueOf(price))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("가격은 0 미만일 수 없습니다.");
        }
    }

    @ParameterizedTest
    @CsvSource({"0, true", "1, false", "2, false"})
    void 돈이_특정_금액을_초과하는지_확인한다(long price, boolean expected) {
        // given
        Money money = Money.valueOf(BigDecimal.ONE);

        // when
        boolean actual = money.isGreaterThan(BigDecimal.valueOf(price));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

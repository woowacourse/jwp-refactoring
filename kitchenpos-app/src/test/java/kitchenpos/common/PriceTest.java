package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.vo.Price;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class PriceTest {

    @Nested
    class from_성공_테스트 {
    }

    @Nested
    class from_실패_테스트 {

        @NullSource
        @ParameterizedTest
        void 금액이_NULL이면_에러를_반환한다(final BigDecimal price) {
            // given & when & then
            assertThatThrownBy(() -> Price.from(price))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 금액이 없거나, 음수입니다.");
        }

        @Test
        void 금액이_음수이면_에러를_반환한다() {
            // given
            final var price = BigDecimal.valueOf(-300L);

            // when & then
            assertThatThrownBy(() -> Price.from(price))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 금액이 없거나, 음수입니다.");
        }
    }

    @Nested
    class createZero_실패_테스트 {
    }

    @Nested
    class plus_성공_테스트 {

        @Test
        void 더하기를_할_수_있다() {
            // given
            final var price1 = Price.from(BigDecimal.valueOf(100L));
            final var price2 = Price.from(BigDecimal.valueOf(500L));

            final var expected = Price.from(BigDecimal.valueOf(600L));

            // when
            final var actual = price1.plus(price2);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class plus_실패_테스트 {
    }

    @Nested
    class multiply_성공_테스트 {

        @Test
        void 곱하기를_할_수_있다() {
            // given
            final var price = Price.from(BigDecimal.valueOf(100L));

            final var expected = Price.from(BigDecimal.valueOf(500L));

            // when
            final var actual = price.multiply(5L);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class multiply_실패_테스트 {
    }
}

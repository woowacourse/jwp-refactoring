package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
}

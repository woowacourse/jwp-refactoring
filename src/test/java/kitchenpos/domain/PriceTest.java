package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가격을_n배한다() {
        // given
        Price price = new Price(new BigDecimal(1_000));

        // when
        Price result = price.multiply(2L);

        // then
        assertThat(result).isEqualTo(new Price(new BigDecimal(2_000)));
    }

    @Test
    void 가격을_더한다() {
        // given
        Price price1 = new Price(new BigDecimal(1_000));
        Price price2 = new Price(new BigDecimal(2_000));

        // when
        Price result = price1.add(price2);

        // then
        assertThat(result).isEqualTo(new Price(new BigDecimal(3_000)));
    }

    @Test
    void 가격_비교() {
        // given
        Price price1 = new Price(new BigDecimal(1_000));
        Price price2 = new Price(new BigDecimal(2_000));

        // when
        boolean result = price1.isGreaterThan(price2);

        // then
        assertThat(result).isFalse();
    }

    @Nested
    class 가격_생성 {

        @Test
        void 값이_null이면_예외() {
            // given
            BigDecimal value = null;

            // when & then
            assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴 가격입니다.");
        }

        @Test
        void 값이_음수면_예외() {
            // given
            BigDecimal value = new BigDecimal(-1);

            // when & then
            assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 메뉴 가격입니다.");
        }

        @Test
        void 가격_생성_성공() {
            // given
            BigDecimal value = new BigDecimal(1000);

            // when
            Price price = new Price(value);

            // then
            assertThat(price.getValue()).isEqualTo(new BigDecimal(1000));
        }
    }
}

package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(strings = {"0", "9"})
    @DisplayName("0부터 19자리 수까지 가격을 생성할 수 있다.")
    void 가격_생성_성공(String number) {
        // given
        BigDecimal value = new BigDecimal(number.repeat(19));

        // when
        final Price price = Price.from(value);

        // when
        assertThat(price.value().setScale(0)).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.123", "9999999999999999.248"})
    @DisplayName("가격 생성 시 소수점을 둘째자리까지 처리한다. 나머지 수는 버린다.")
    void 가격_생성_성공_소수점처리(String number) {
        // given
        BigDecimal value = new BigDecimal(number);

        // when
        final Price price = Price.from(value);

        // when
        assertThat(price.value()).isEqualTo(value.setScale(2, RoundingMode.DOWN));
    }

    @Test
    @DisplayName("가격 생성 시 0보다 같거나 큰 수만 가능하다.")
    void 가격_생성_실패_음수() {
        // given
        BigDecimal value = BigDecimal.valueOf(-1);

        // expected
        assertThatThrownBy(() -> Price.from(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격 생성 시 19자리 수까지만 가능하다.")
    void 가격_생성_실패_자리수() {
        // given
        BigDecimal value = new BigDecimal("9".repeat(20));

        // expected
        assertThatThrownBy(() -> Price.from(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class Sum {

        @Test
        void 가격_총합() {
            // given
            final List<Price> values = List.of(
                    Price.from(BigDecimal.ZERO),
                    Price.from(BigDecimal.ONE),
                    Price.from(BigDecimal.TEN)
            );

            // expected
            assertThat(Price.sum(values))
                    .isEqualTo(Price.from(BigDecimal.valueOf(11)));
        }

        @Test
        void 가격_비교() {
            // given
            Price zero = Price.from(BigDecimal.ZERO);
            Price one = Price.from(BigDecimal.ONE);

            // expected
            assertThat(zero.isBiggerThan(one))
                    .isFalse();
        }

        @Test
        void 가격_곱() {
            // given
            Price ten = Price.from(BigDecimal.TEN);

            // expected
            assertThat(ten.multiply(3))
                    .isEqualTo(Price.from(BigDecimal.valueOf(30)));
        }
    }
}

package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("price 값이 음수이면 예외를 발생시킨다.")
    @Test
    void check_price_under_zero() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Price.from(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품 가격은 null 혹은 음수가 될 수 없습니다.");
    }

    @DisplayName("price 값이 null이면 예외를 발생시킨다.")
    @Test
    void check_price_isNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> Price.from(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("상품 가격은 null 혹은 음수가 될 수 없습니다.");
    }

    @DisplayName("현재 자신의 금액보다 큰 금액이 들어오면 true를 반환하고 그렇지 않으면 false를 반환한다.")
    @MethodSource("getPriceAndResult")
    @ParameterizedTest
    void validate_price_isBigger_or_isSmaller(final BigDecimal input, final boolean expect) {
        // given
        final Price price = Price.from(20000);

        // when
        final boolean result = price.isBigger(input);

        // then
        assertThat(result).isEqualTo(expect);
    }

    private static Stream<Arguments> getPriceAndResult() {
        return Stream.of(
            Arguments.of(new BigDecimal(10000), true),
            Arguments.of(new BigDecimal(30000), false)
        );
    }
}

package kitchenpos.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> Price.from("1000"))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 0 이상의 가격을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "10", "1000", "100000000"})
    void success_create_isZeroOrPositive(final String zeroOrPositiveValue) {
        assertThatCode(() -> Price.from(zeroOrPositiveValue))
                .doesNotThrowAnyException();
    }

    @DisplayName("[EXCEPTION] 0 미만의 가격을 생성할 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"-1", "-2", "-3", "-10", "-100"})
    void throwException_create_when_isNegative(final String negativeValue) {
        assertThatThrownBy(() -> Price.from(negativeValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[SUCCESS] 가격을 합한다.")
    @Test
    void success_sum() {
        // given
        final Price price = Price.from("1000");

        // when
        final Price actual = price.sum(Price.from("9000"));

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(new BigDecimal("10000"));
    }

    @DisplayName("[SUCCESS] 가격을 수랑만큼 곱한다.")
    @Test
    void success_multiply() {
        // given
        final Price price = Price.from("1000");

        // when
        final Price actual = price.multiply(new Quantity(10));

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(new BigDecimal(10000));
    }

    @DisplayName("[SUCCESS] 현재 가격보다 큰지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000:0:true", "1000:999:true", "1000:1000:false", "1000:1001:false"}, delimiter = ':')
    void success_isGreaterThan(final String currentPrice, final String targetPrice, final boolean expected) {
        // given
        final Price current = Price.from(currentPrice);
        final Price target = Price.from(targetPrice);


        // when
        final boolean actual = current.isGreaterThan(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

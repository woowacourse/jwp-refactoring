package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.menu.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격을 생성할 때, 값이 없으면 예외가 발생한다.")
    @Test
    void constructor_throwsException_ifNoPrice() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Price(null));
    }

    @DisplayName("가격을 생성할 때, 값이 0보다 작으면 예외가 발생한다.")
    @ValueSource(ints = {-1, -500, -1000})
    @ParameterizedTest
    void constructor_throwsException_ifPriceUnder0(final int value) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Price.from(value));
    }

    @DisplayName("여러 가격의 총 합을 구한다.")
    @CsvSource({"500,1000,1500", "550,400,950"})
    @ParameterizedTest(name = "{0}원, {1}원의 합은 {2}이다.")
    void sum(final int value1, final int value2, final long expected) {
        final List<Price> prices = Arrays.asList(Price.from(value1), Price.from(value2));
        final Price actual = Price.sum(prices);

        assertThat(actual.getValue())
                .isEqualTo(BigDecimal.valueOf(expected));
    }

    @DisplayName("가격을 곱한다.")
    @CsvSource({"1000,1,1000", "500,2,1000", "600,3,1800"})
    @ParameterizedTest(name = "{0}원에 {1}를 곱하면 {2}원이다.")
    void multiply(final int value, final long multiplicand, final long expected) {
        final Price price = Price.from(value);
        final Price multipliedPrice = price.multiply(multiplicand);

        assertThat(multipliedPrice.getValue())
                .isEqualTo(BigDecimal.valueOf(expected));
    }

    @DisplayName("가격이 다른 가격에 비해 큰지 비교한다.")
    @CsvSource({"100,101,false", "100,99,true"})
    @ParameterizedTest(name = "{0}원에 대해 {1}원보다 큰지 비교하면 결과는 {2}이다.")
    void isOver(final int value, final int otherValue, final boolean expected) {
        final Price price = Price.from(value);
        final Price otherPrice = Price.from(otherValue);

        assertThat(price.isOver(otherPrice)).isEqualTo(expected);
    }
}

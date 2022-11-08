package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class PriceTest {

    @ParameterizedTest
    @NullSource
    @CsvSource({"-1"})
    @DisplayName("음수나 Null로 생성할 수 없다.")
    void construct_exceptionNegativeOrNull(final BigDecimal value) {
        // given, when, then
        assertThatThrownBy(() -> new Price(value))
                .isExactlyInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("가격의 합을 구할 수 있다.")
    void add() {
        // given
        final Price price = new Price(1000L);

        // when
        final Price actual = price.add(new Price(1L));

        // then
        assertThat(actual).isEqualTo(new Price(1001L));
    }

    @ParameterizedTest
    @CsvSource(value = {"1000,999,true", "1000,1001,false"})
    @DisplayName("가격이 더 비싼 지 판별할 수 있다.")
    void isExpansiveThan(final Long from, final Long to, final boolean expect) {
        // given
        final Price price = new Price(from);

        // when
        final boolean actual = price.isExpansiveThan(new Price(to));

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("가격의 곱을 할 수 있다.")
    void multiply() {
        // given
        final Price price = new Price(1000L);

        // when
        final Price actual = price.multiply(2);

        // then
        assertThat(actual).isEqualTo(new Price(2000L));
    }
}

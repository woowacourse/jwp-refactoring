package kitchenpos.domain;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("가격을 더할 수 있다.")
    @Test
    void add() {
        // given
        final Price price1 = new Price(valueOf(1000));
        final Price price2 = new Price(valueOf(2000));

        // when
        final Price resultPrice = price1.add(price2);

        // then
        assertThat(resultPrice).isEqualTo(new Price(valueOf(3000)));
    }

    @DisplayName("가격을 곱할 수 있다.")
    @Test
    void multiply() {
        // given
        final Price price = new Price(valueOf(1000));

        // when
        final Price resultPrice = price.multiply(3);

        // then
        assertThat(resultPrice).isEqualTo(new Price(valueOf(3000)));
    }

    @DisplayName("특정 가격의 금액이 비교값보다 크면 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"500", "100", "10"})
    void isGreaterThan(final BigDecimal value) {
        // given
        final Price price = new Price(valueOf(1000));

        // when & then
        assertThat(price.isGreaterThan(new Price(value))).isTrue();
    }

    @DisplayName("특정 가격의 금액이 비교값보다 크지 않으면 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1000", "1500", "2000"})
    void isGreaterThan_FailWhenPriceLessThanValue(final BigDecimal value) {
        // given
        final Price price = new Price(valueOf(1000));

        // when & then
        assertThat(price.isGreaterThan(new Price(value))).isFalse();
    }
}

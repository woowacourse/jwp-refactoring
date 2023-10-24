package kitchenpos.menu.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @Test
    @DisplayName("Price를 생성할 때 음수값을 입력하면 예외를 반환한다.")
    void Price_fail_negative() {
        //given, when
        final ThrowingCallable actual = () -> new Price(BigDecimal.valueOf(-3000));

        //then
        Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Price를 생성할 때 null 값을 입력하면 예외를 반환한다.")
    void Price_fail_null() {
        //given, when
        final ThrowingCallable actual = () -> new Price(null);

        //then
        Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"300:400:700", "500:500:1000", "200:300:500"}, delimiter = ':')
    @DisplayName("add() 메서드를 호출하면 두 값을 더한 가격을 얻을 수 있다.")
    void add(final BigDecimal base, final BigDecimal addition, final BigDecimal expected) {
        //given
        final Price basePrice = new Price(base);
        final Price additionPrice = new Price(addition);
        //when
        final Price actual = basePrice.add(additionPrice);

        //then
        assertThat(actual).isEqualTo(new Price(expected));
    }

    @ParameterizedTest
    @CsvSource(value = {"300:4:1200", "500:5:2500", "200:3:600"}, delimiter = ':')
    @DisplayName("multiply() 메서드를 호출하면 값에 횟수를 더한 가격을 얻을 수 있다.")
    void multiply(final BigDecimal base, final long count, final BigDecimal expected) {
        //given
        final Price basePrice = new Price(base);
        //when
        final Price actual = basePrice.multiply(count);

        //then
        assertThat(actual).isEqualTo(new Price(expected));
    }

    @Test
    @DisplayName("isGreaterThan()을 호출했을 때, 기준 가격이 입력 받은 가격보다 비싸면 true를 반환한다.")
    void isGreaterThan() {
        //given
        final Price criteria = new Price(BigDecimal.valueOf(300L));
        final Price compare = new Price(BigDecimal.valueOf(200L));

        //when
        final boolean actual = criteria.isGreaterThan(compare);

        //then
        assertThat(actual).isTrue();
    }


}
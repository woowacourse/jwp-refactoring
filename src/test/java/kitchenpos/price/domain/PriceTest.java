package kitchenpos.price.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class PriceTest {
    @DisplayName("Price 정상 생성 확인한다.")
    @Test
    void constructorTest() {
        assertDoesNotThrow(() -> new Price(BigDecimal.TEN));
    }

    @DisplayName("Price value가 null이거나 음수인 경우 예외를 반환한다.")
    @MethodSource("priceArgumentMethod")
    @ParameterizedTest
    void constructorTest2(final BigDecimal value) {
        assertThatThrownBy(() -> new Price(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("금액이 유효하지 않습니다.");
    }

    @DisplayName("add가 정상적으로 동작하는지 확인한다.")
    @Test
    void addTest() {
        final Price zero = new Price(BigDecimal.ZERO);
        final Price one = new Price(BigDecimal.ONE);

        assertThat(zero.add(one)).isEqualTo(new Price(BigDecimal.ONE));
    }

    @DisplayName("multiplesOf가 정상적으로 동작하는지 확인한다.")
    @Test
    void multiplesOfTest() {
        final Price ten = new Price(BigDecimal.TEN);
        final long zero = 0;

        assertThat(ten.multiplesOf(zero)).isEqualTo(new Price(BigDecimal.ZERO));
    }

    private static Stream<BigDecimal> priceArgumentMethod() {
        return Stream.of(null, BigDecimal.valueOf(-10000));
    }
}

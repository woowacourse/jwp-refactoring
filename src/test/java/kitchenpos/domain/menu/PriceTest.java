package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {
    static Stream<Arguments> invalidPrices() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(BigDecimal.valueOf(-1000))
        );
    }

    static Stream<Arguments> comparePrices() {
        return Stream.of(
                Arguments.of(Price.of(BigDecimal.valueOf(900)), true),
                Arguments.of(Price.of(BigDecimal.valueOf(1000)), false),
                Arguments.of(Price.of(BigDecimal.valueOf(1200)), false)
        );
    }

    @DisplayName("유효하지 않은 값으로 price 생성시 예외 출력")
    @ParameterizedTest
    @MethodSource("invalidPrices")
    void constructPriceWithInvalidTest(BigDecimal input) {
        assertThatThrownBy(() -> {
            Price.of(input);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("0원을 나타내는 price 생성")
    @Test
    void zeroTest() {
        Price zero = Price.zero();

        assertThat(zero.getPrice()).isEqualTo(BigDecimal.ZERO);
    }

    @DisplayName("가격과 수량을 입력했을 때 해당 가격 곱하기 수량 만큼 더한다")
    @Test
    void addWithProductQuantityTest() {
        Price zero = Price.zero();
        Price input = Price.of(BigDecimal.valueOf(1_000));
        long quantity = 2L;

        Price result = zero.addTotalPrice(input, quantity);

        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(2_000));
    }

    @DisplayName("가격의 크기를 비교한다")
    @ParameterizedTest
    @MethodSource("comparePrices")
    void biggerThanTest(Price input, boolean result) {
        Price price = Price.of(BigDecimal.valueOf(1_000));

        assertThat(price.biggerThan(input)).isEqualTo(result);
    }
}

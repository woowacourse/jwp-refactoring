package kitchenpos.domain.price;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.domain.quantity.Quantity;
import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Price 도메인 단위테스트")
class PriceTest {

    static Stream<Arguments> isGreaterOrEqualThan() {
        return Stream.of(
            Arguments.of(0, 0, true),
            Arguments.of(1, 1, true),
            Arguments.of(4, 4, true),
            Arguments.of(1, 0, true),
            Arguments.of(5, 4, true),
            Arguments.of(0, 1, false),
            Arguments.of(4, 5, false)
        );
    }

    static Stream<Arguments> multiply() {
        return Stream.of(
            Arguments.of(0, 0L),
            Arguments.of(0, 5L),
            Arguments.of(5, 0L),
            Arguments.of(5, 5L),
            Arguments.of(5, 1L),
            Arguments.of(1, 5L)
        );
    }

    @DisplayName("생성 - 성공 - value가 null 또는 음수가 아닐 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 100, 1_000, 1_000_000_000})
    void create_Success_When_ValueIsValid(int value) {
        // given
        // when
        // then
        assertThatCode(() -> new Price(value))
            .doesNotThrowAnyException();
    }

    @DisplayName("생성 - 실패 - value가 음수 또는 null일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {-1_000_000, -1_000, -1})
    @NullSource
    void create_Fail_When_ValueIsNegativeOrNull(Integer value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Price(value))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("price 값 크기 비교 - 성공 - price >= otherPrice 일 때 True, 그 외 False 반환")
    @CustomParameterizedTest
    @MethodSource
    void isGreaterOrEqualThan(int priceValue, int otherPriceValue, boolean expectedResult) {
        // given
        final Price price = new Price(priceValue);
        final Price otherPrice = new Price(otherPriceValue);

        // when
        // then
        assertThat(price.isGreaterOrEqualThan(otherPrice)).isEqualTo(expectedResult);
    }

    @DisplayName("곱셈 결과 반환 - 성공")
    @CustomParameterizedTest
    @MethodSource
    void multiply(int priceValue, long quantityValue) {
        // given
        final Price price = new Price(priceValue);
        final Quantity quantity = new Quantity(quantityValue);
        final Price expectedResult = new Price(priceValue * (int) quantityValue);

        // when
        // then
        assertThat(price.multiply(quantity)).isEqualTo(expectedResult);
    }
}

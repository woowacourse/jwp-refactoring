package kitchenpos.refactoring.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void 가격이_0원_이상인_경우_정상_등록(int input) {
        // given
        // when
        BigDecimal priceValue = BigDecimal.valueOf(input);
        Price price = new Price(priceValue);

        // then
        assertThat(price.getPrice()).isEqualTo(priceValue);
    }

    @ParameterizedTest
    @MethodSource("bigDecimalValues")
    @NullSource
    void 가격이_0원_미만인_경우_예외_발생(BigDecimal value) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<BigDecimal> bigDecimalValues() {
        return Stream.of(
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(-10)
        );
    }
}

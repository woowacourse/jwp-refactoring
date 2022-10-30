package kitchenpos.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

class PriceTest {

    @ParameterizedTest
    @NullSource
    @MethodSource("invalidValue")
    void validate(final BigDecimal value) {
        assertThatThrownBy(() -> new Price(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 null 이거나 0원 미만일 수 없습니다.");
    }

    private static Stream<Arguments> invalidValue() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1))
        );
    }
}

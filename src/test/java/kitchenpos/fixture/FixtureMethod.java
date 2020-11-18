package kitchenpos.fixture;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class FixtureMethod {
    public static Stream<Arguments> provideNullAndNegativeLongValue() {
        return Stream.of(
                null,
                Arguments.of(BigDecimal.valueOf(-1L))
        );
    }
}

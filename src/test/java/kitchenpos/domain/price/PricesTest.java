package kitchenpos.domain.price;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.config.CustomParameterizedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Prices 도메인 단위테스트")
class PricesTest {

    static Stream<Arguments> sum() {
        return Stream.of(
            Arguments.of(Collections.emptyList(), 0),
            Arguments.of(Collections.singletonList(new Price(0)), 0),
            Arguments.of(Collections.singletonList(new Price(1)), 1),
            Arguments.of(Collections.singletonList(new Price(1_000)), 1_000),
            Arguments.of(Arrays.asList(
                new Price(0),
                new Price(1),
                new Price(1_000)
            ), 1_001),
            Arguments.of(Arrays.asList(
                new Price(100),
                new Price(1_000),
                new Price(1_000_000)
            ), 1_001_100)
        );
    }

    @DisplayName("Price들의 합을 반환한다")
    @CustomParameterizedTest
    @MethodSource
    void sum(List<Price> pricesArg, int expectedPricesSumValue) {
        // given
        final Prices prices = new Prices(pricesArg);
        final Price expectedResult = new Price(expectedPricesSumValue);

        // when
        // then
        assertThat(prices.sum()).isEqualTo(expectedResult);
    }
}

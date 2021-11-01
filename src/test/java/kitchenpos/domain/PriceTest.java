package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @ValueSource(doubles = {-1, -5.9999, -1000000000})
    void 생성_시_음수_이면_예외를_반환한다(double input) {
        BigDecimal param = BigDecimal.valueOf(input);

        assertThrows(IllegalArgumentException.class, () -> new Price(param));
    }

    @ParameterizedTest
    @CsvSource(value = {"1,1,2", "1.00001,1.99999,3.00000", "10001,10001,20002"})
    void 값을_더한다(double first, double second, double expected) {
        Price price1 = new Price(BigDecimal.valueOf(first));
        Price price2 = new Price(BigDecimal.valueOf(second));
        Price expectedPrice = new Price(BigDecimal.valueOf(expected));

        assertThat(price1.sum(price2)).isEqualTo(expectedPrice);
    }

    @ParameterizedTest
    @CsvSource(value = {"2,1,true", "1,2,false", "1,1,false"})
    void 큰_값을_비교한다(int first, int second, boolean expected) {
        Price price1 = new Price(BigDecimal.valueOf(first));
        Price price2 = new Price(BigDecimal.valueOf(second));

        assertThat(price1.isGreater(price2)).isEqualTo(expected);
    }
}
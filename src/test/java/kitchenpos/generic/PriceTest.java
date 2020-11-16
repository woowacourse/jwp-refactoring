package kitchenpos.generic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @DisplayName("Price의 BigDecimal보다 작은지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,false", "100,false", "101,true"})
    void isLessThan(Long value, boolean expect) {
        Price price = new Price(BigDecimal.valueOf(100L));

        boolean result = price.isLessThan(value);

        assertThat(result).isEqualTo(expect);
    }
}
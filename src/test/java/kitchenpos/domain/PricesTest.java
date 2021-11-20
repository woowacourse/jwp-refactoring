package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Prices 단위 테스트")
class PricesTest {

    @DisplayName("모든 Price들의 값을 더한 결과를 반환한다.")
    @Test
    void sumAll() {
        // given
        Price price1 = new Price(BigDecimal.valueOf(3_000));
        Price price2 = new Price(BigDecimal.valueOf(4_000));
        Price price3 = new Price(BigDecimal.valueOf(5_000));

        Prices prices = new Prices(Arrays.asList(price1, price2, price3));

        // when
        Price totalPrice = prices.sumAll();
        Price expectedPrice = new Price(price1.getValue().add(price2.getValue()).add(price3.getValue()));

        // then
        assertThat(totalPrice).isEqualTo(expectedPrice);
    }
}

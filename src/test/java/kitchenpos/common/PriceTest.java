package kitchenpos.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.common.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @Test
    void 생성_시_음수_이면_예외를_반환한다() {
        BigDecimal param = BigDecimal.valueOf(-1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Price(param));
        assertThat(exception.getMessage()).isEqualTo("가격 값이 유효하지 않습니다.");
    }

    @Test
    void 값을_더한다() {
        Price price1 = new Price(BigDecimal.valueOf(1));
        Price price2 = new Price(BigDecimal.valueOf(2));
        Price expectedPrice = new Price(BigDecimal.valueOf(3));

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
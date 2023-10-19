package kitchenpos.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class PriceTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        assertThatCode(() -> new Price("1000"))
                .doesNotThrowAnyException();
    }

    @DisplayName("[SUCCESS] 가격을 수랑만큼 곱한다.")
    @Test
    void success_multiply() {
        // given
        final Price price = new Price("1000");

        // when
        final Price actual = price.multiply(new Quantity(10));

        // then
        assertThat(actual.getValue()).isEqualByComparingTo(new BigDecimal(10000));
    }

    @DisplayName("[SUCCESS] 현재 가격보다 큰지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1000:0:true", "1000:999:true", "1000:1000:false", "1000:1001:false"}, delimiter = ':')
    void success_isGreaterThan(final String currentPrice, final String targetPrice, final boolean expected) {
        // given
        final Price current = new Price(currentPrice);
        final Price target = new Price(targetPrice);


        // when
        final boolean actual = current.isGreaterThan(target);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

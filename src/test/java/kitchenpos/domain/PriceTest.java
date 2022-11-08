package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.price.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @Test
    void construct_가격이_null이면_예외를_반환한다() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -1000, -10000})
    void construct_가격이_음수이면_예외를_반환한다(int number) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(number)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("들어온 수량에 따른 총합 가격을 계산한다.")
    @Test
    void multiply() {
        //given
        Price price = new Price(BigDecimal.valueOf(10000));

        //when
        int quantity = 10;
        BigDecimal actual = price.multiply(10);

        //then
        BigDecimal expected = BigDecimal.valueOf(100000);
        assertThat(actual).isEqualTo(expected);
    }
}

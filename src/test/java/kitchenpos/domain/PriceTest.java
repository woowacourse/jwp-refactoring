package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.exception.NoPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @Test
    @DisplayName("가격이 0원 이상인 경우에 Price를 생성할 수 있다.")
    void create_success() {
        assertThatCode(() -> Price.from(BigDecimal.valueOf(0)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가격이 0원 미만인 경우에 Price를 생성할 수 없다.")
    void create_fail1() {
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(-1)))
                .isInstanceOf(NoPriceException.class);
    }

    @Test
    @DisplayName("가격이 null인 경우에 Price를 생성할 수 없다.")
    void create_fail2() {
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(NoPriceException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"10,10,true", "10,9,false"})
    @DisplayName("가격이 동일한 객체는 동등하고, 가격이 다르면 두 객체는 동등하지 않다.")
    void create_success2(BigDecimal value1, BigDecimal value2, boolean expected) {
        Price price1 = Price.from(value1);
        Price price2 = Price.from(value2);

        assertThat(price1.equals(price2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"100,101,false", "100,100,false", "101,100,true"})
    @DisplayName("현재가격이 주어진 가격보다 더 큰지를 알 수 있다.")
    void isBiggerThan(BigDecimal one, BigDecimal other, boolean expected) {
        Price price = Price.from(one);

        assertThat(price.isBiggerThan(other)).isEqualTo(expected);
    }
}

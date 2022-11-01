package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.exception.EmptyDataException;
import kitchenpos.common.exception.LowerThanZeroPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {

    @DisplayName("null로 Price를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_Null() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(EmptyDataException.class)
                .hasMessageContaining(Price.class.getSimpleName())
                .hasMessageContaining("입력되지 않았습니다.");
    }

    @DisplayName("0보다 작은 값으로 Price를 생성하려고 하면 예외를 발생시킨다.")
    @Test
    void from_Exception_InvalidPrice() {
        BigDecimal invalidPrice = new BigDecimal(-1);

        assertThatThrownBy(() -> new Price(invalidPrice))
                .isInstanceOf(LowerThanZeroPriceException.class);
    }

    @DisplayName("다른 가격을 받아 자신의 가격이 더 비싼지 반환한다.")
    @ParameterizedTest
    @CsvSource({"999, true", "1001, false"})
    void isHigher(int another, boolean expected) {
        Price price = new Price(new BigDecimal(1000));

        boolean actual = price.isHigher(new BigDecimal(another));

        assertThat(actual).isEqualTo(expected);
    }
}

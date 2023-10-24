package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PriceTest {
    @ParameterizedTest
    @ValueSource(ints = {10, 100, 1000, 10000})
    void create(final int price) {
        //when&then
        assertDoesNotThrow(() -> new Price(BigDecimal.valueOf(price)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void validateNegative(int price) {
        //when&then
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(price)))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("잘못된 가격입니다.");
    }

    @Test
    void validateNull() {
        //when&then
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("잘못된 가격입니다.");
    }
}

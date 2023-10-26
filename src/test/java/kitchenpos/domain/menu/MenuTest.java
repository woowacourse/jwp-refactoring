package kitchenpos.domain.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MenuTest {

    @ParameterizedTest
    @NullSource
    @CsvSource(value = {"-1", "-2", "-100000"})
    @DisplayName("상품 가격이 null이거나 0 미만이면 IllegalArgumentException이 발생한다.")
    void smallerThenZeroPriceTest(final BigDecimal price) {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> new Menu("메뉴", price, null, null));
    }
}

package kitchenpos.domain;

import static kitchenpos.application.ServiceTestFixture.MENU_PRODUCTS;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void throw_exception_when_price_is_null() {
        assertThatThrownBy(() -> new Menu("순삭치킨", null, 1L, MENU_PRODUCTS))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throw_exception_when_price_is_smaller_than_zero() {
        assertThatThrownBy(() -> new Menu("순삭치킨", BigDecimal.valueOf(-1), 1L, MENU_PRODUCTS))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validatePriceGreaterThan_fail() {
        Menu menu = new Menu("순삭치킨", BigDecimal.valueOf(10000), 1L, MENU_PRODUCTS);

        assertThatThrownBy(() -> menu.validatePriceGreaterThan(BigDecimal.valueOf(9000)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validatePriceGreaterThan_success() {
        Menu menu = new Menu("순삭치킨", BigDecimal.valueOf(10000), 1L, MENU_PRODUCTS);

        assertThatCode(() -> menu.validatePriceGreaterThan(BigDecimal.valueOf(11000)))
                .doesNotThrowAnyException();
    }
}
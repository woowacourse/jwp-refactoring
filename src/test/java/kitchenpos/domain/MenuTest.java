package kitchenpos.domain;

import static kitchenpos.application.ServiceTestFixture.MENU_PRODUCTS;
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
    void throw_exception_when_price_is_greater_than_AllMenuProductSum() {
        assertThatThrownBy(() -> new Menu("순삭치킨", BigDecimal.valueOf(33000), 1L, MENU_PRODUCTS))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
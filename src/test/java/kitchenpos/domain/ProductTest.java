package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void throw_exception_when_price_is_smaller_than_zero() {
        assertThatThrownBy(() -> new Product("치킨", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throw_exception_when_price_is_null() {
        assertThatThrownBy(() -> new Product("치킨", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
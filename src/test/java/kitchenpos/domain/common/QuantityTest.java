package kitchenpos.domain.common;

import kitchenpos.domain.DomainTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuantityTest extends DomainTest {
    @Test
    void throw_when_quantity_is_below_zero() {
        // when & then
        assertThatThrownBy(() -> Quantity.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_BELOW_ZERO_ERROR_MESSAGE);
    }

}
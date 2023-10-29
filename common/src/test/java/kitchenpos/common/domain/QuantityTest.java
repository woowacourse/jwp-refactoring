package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QuantityTest {
    @Test
    void throw_when_quantity_is_below_zero() {
        // when & then
        assertThatThrownBy(() -> Quantity.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Quantity.PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE);
    }

}
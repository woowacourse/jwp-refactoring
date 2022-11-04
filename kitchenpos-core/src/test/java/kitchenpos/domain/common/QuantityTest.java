package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.badrequest.NotPositiveQuantityException;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void 수량은_0개_이하일_수_없다() {
        assertThatThrownBy(() -> new Quantity(0)).isInstanceOf(NotPositiveQuantityException.class);
    }
}

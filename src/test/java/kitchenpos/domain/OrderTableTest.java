package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void validateTableGroupIdIsNull() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 0, false);

        //when, then
        Assertions.assertThatThrownBy(orderTable::validateTableGroupIdIsNull)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validateOrderTableIsNotEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, true);

        //when, then
        Assertions.assertThatThrownBy(orderTable::validateIsNotEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.INVALID_CHANGE_NUMBER_OF_GUEST;

import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 게스트_숫자가_음수일_경우_예외를_반환한다() {
        Assertions.assertThatThrownBy(() -> new OrderTable(1L, null, -1, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CHANGE_NUMBER_OF_GUEST.getMessage());
    }
}

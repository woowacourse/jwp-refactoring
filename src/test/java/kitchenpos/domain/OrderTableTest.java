package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 주문_테이블_생성() {
        Assertions.assertDoesNotThrow(() -> OrderTable.of(1, true));
    }

    @Test
    void 손님의_수가_음수인_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> OrderTable.of(-1, true));
    }

    @Test
    void 테이블집합이_속해있는_경우_테이블을_비울_수_없다() {
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹에 속해 있어 테이블을 비우지 못합니다.");
    }
}

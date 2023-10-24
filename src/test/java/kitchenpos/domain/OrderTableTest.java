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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹이 존재하는 테이블은 empty를 변경할 수 없습니다.");
    }

    @Test
    void validateOrderTableIsNotEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(null, 0, true);

        //when, then
        Assertions.assertThatThrownBy(orderTable::validateIsNotEmpty)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 비어있을 수 없습니다.");
    }
}

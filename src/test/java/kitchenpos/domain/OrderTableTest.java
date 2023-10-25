package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    @DisplayName("빈 테이블 여부를 바꿀 때 그룹 지정된 테이블이면 예외가 발생한다")
    void changeEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 1L, 5, true);

        //when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("그룹 지정된 테이블은 빈 테이블 여부를 바꿀 수 없습니다.");
    }

    @Test
    @DisplayName("방문자 수를 바꿀 때 음수로 바꾸려고 하면 예외가 발생한다")
    void changeNumberOfGuests() {
        //given
        final OrderTable orderTable = new OrderTable(4, false);

        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문자 수는 음수일 수 없습니다.");
    }

    @Test
    @DisplayName("방문자 수를 바꿀 때 빈 테이블이면 예외가 발생한다")
    void changeNumberOfGuests2() {
        //given
        final OrderTable orderTable = new OrderTable(4, true);

        //when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("빈 테이블의 방문자 수를 바꿀 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블이 아닌 지 검사 후 빈 테이블이면 예외를 발생시킨다")
    void validateNonEmpty() {
        //given
        final OrderTable orderTable = new OrderTable(4, true);

        //when, then
        assertThatThrownBy(orderTable::validateNotEmpty)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("빈 주문 테이블입니다.");
    }

    @Test
    @DisplayName("그룹 지정할 때 빈 테이블이 아니면 예외가 발생한다")
    void changeTableGroupId() {
        //given
        final OrderTable orderTable = new OrderTable(4, false);

        //when, then
        assertThatThrownBy(() -> orderTable.groupBy(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 빈 테이블이 아니거나 이미 그룹 지정된 테이블입니다.");
    }

    @Test
    @DisplayName("그룹 지정할 때 이미 그룹 지정된 테이블이면 예외가 발생한다")
    void changeTableGroupId2() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 1L, 4, true);

        //when, then
        assertThatThrownBy(() -> orderTable.groupBy(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 빈 테이블이 아니거나 이미 그룹 지정된 테이블입니다.");
    }
}

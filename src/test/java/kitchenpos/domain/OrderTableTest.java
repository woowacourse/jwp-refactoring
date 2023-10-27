package kitchenpos.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @DisplayName("테이블을 비운다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("단체로 지정된 테이블을 비우면 예외가 발생한다.")
    @Test
    void changeEmpty_TableGroup_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(null, 1L, 5, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        int expected = 7;
        OrderTable orderTable = new OrderTable(5, false);

        // when
        orderTable.changeNumberOfGuests(expected);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @DisplayName("테이블의 손님 수를 0명 미만으로 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_LessThanZero_ExceptionThrown() {
        // given
        OrderTable orderTable = new OrderTable(5, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어 있는 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_EmptyTable_ExceptionThrown() {
        // given
        OrderTable emptyOrderTable = new OrderTable(5, true);

        // when, then
        assertThatThrownBy(() -> emptyOrderTable.changeNumberOfGuests(6))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 그룹으로 묶는다.")
    @Test
    void group() {
        // given
        Long tableGroupId = 5L;
        OrderTable orderTable = new OrderTable();

        // when
        orderTable.group(tableGroupId);

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

}

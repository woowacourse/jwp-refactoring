package kitchenpos.ordertable.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {
    @DisplayName("빈 테이블 설정 및 해지")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean status) {
        OrderTable orderTable = new OrderTable(null, null, 2, !status);

        orderTable.changeEmpty(status);

        assertThat(orderTable.isEmpty()).isEqualTo(status);
    }

    @DisplayName("그룹으로 지정된 테이블 상태 변경 시 예외 발생")
    @Test
    void changeEmpty_WithTableGroup_ThrownException() {
        OrderTable orderTable = new OrderTable(null, 1L, 2, false);
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("그룹으로 지정된 테이블은 변경할 수 없습니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(null, null, 1, false);

        orderTable.changeNumberOfGuests(4);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("손님 수 변경 시, 음수면 예외가 발생한다")
    @Test
    void changeNumberOfGuests_WithNegativeNumber_ThrownException() {
        OrderTable orderTable = new OrderTable(null, null, 1, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 음수일 수 없습니다.");
    }

    @DisplayName("손님 수 변경 시, 빈 테이블이면 예외가 발생한다")
    @Test
    void changeNumberOfGuests_WithEmptyTable_ThrownException() {
        OrderTable orderTable = new OrderTable(null, null, 0, true);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비어있는 테이블에는 손님 수를 설정할 수 없습니다.");
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void group() {
        OrderTable orderTable = new OrderTable(null, null, 0, true);
        orderTable.group(1L);

        assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("단체 지정시, 테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void group_WithNotEmpty_ThrownException() {
        OrderTable orderTable = new OrderTable(null, null, 2, false);

        assertThatThrownBy(() -> orderTable.group(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비어있지 않은 테이블은 그룹지정을 할 수 없습니다.");
    }

    @DisplayName("단체 지정시, 이미 그룹지정이 되어 있으면 예외가 발생한다.")
    @Test
    void group_WithAlreadyInGroup_ThrownException() {
        OrderTable orderTable = new OrderTable(null, 1L, 0, false);

        assertThatThrownBy(() -> orderTable.group(2L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 그룹 지정이 된 테이블입니다.");
    }
}
package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("상태를 변경한다")
    @Test
    void changeEmptyStatus() {
        boolean expected = true;
        OrderTable orderTable = new OrderTable(10, false);

        orderTable.changeEmptyStatus(expected);
        boolean actual = orderTable.isEmpty();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테이블 그룹이 형성된 경우 상태 변경이 안 된다")
    @Test
    void validateNotBelongToTableGroup() {
        boolean status = true;
        OrderTable orderTable = new OrderTable(10, false);
        orderTable.joinTableGroup(new TableGroup());

        assertThatThrownBy(() -> orderTable.changeEmptyStatus(status))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 테이블 그룹이 형성된 테이블입니다.");
    }

    @DisplayName("손님 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        int numOfGuests = 11;
        OrderTable orderTable = new OrderTable(10, false);

        orderTable.changeNumberOfGuests(numOfGuests);
        int actual = orderTable.getNumberOfGuests();
        assertThat(actual).isEqualTo(numOfGuests);
    }

    @DisplayName("주문 테이블이 비어있는 경우 손님 수 변경이 안된다")
    @Test
    void validateOrderTableIsNotEmpty() {
        boolean isEmpty = true;
        OrderTable orderTable = new OrderTable(10, isEmpty);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 비어있습니다.");
    }

    @DisplayName("0명보다 적은 손님으로는 손님 수 변경이 불가하다")
    @Test
    void validateMinimunGuest() {
        int numOfGuests = -1;
        OrderTable orderTable = new OrderTable(10, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블에는 0명 이상의 손님이 앉을 수 있습니다.");
    }
}

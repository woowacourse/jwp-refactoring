package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void changeEmptyStatus() {
        OrderTable orderTable = new OrderTable(null, 10, false);

        orderTable.changeEmptyStatus(true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeEmptyStatusThrownExceptionWhenEmptyTableGroup() {
        OrderTable orderTable = new OrderTable(1L, 10, false);

        assertThatThrownBy(() -> orderTable.changeEmptyStatus(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 그룹 테이블은 변경할 수 없습니다.");

    }

    @Test
    void matchSize() {
        int input = 20;
        OrderTable orderTable = new OrderTable(null, 10, false);

        orderTable.changeNumberOfGuest(input);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(input);
    }

    @Test
    void matchSizeThrowExceptionWhenNotCollectGuestNumber() {
        int input = 0;
        OrderTable orderTable = new OrderTable(null, 10, false);

        assertThatThrownBy(() -> orderTable.changeNumberOfGuest(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 0명 미만일 수 없습니다.");
    }

    @Test
    void validateOrderTable() {
        OrderTable orderTable = new OrderTable(1L, 10, false);

        assertThatThrownBy(orderTable::validateOrderTable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테이블이거나 주문 테이블이 비어있지 않습니다.");
    }
}

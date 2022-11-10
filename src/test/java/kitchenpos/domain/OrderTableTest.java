package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        // when
        orderTable.changeNumberOfGuests(8);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(8);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 때 0보다 작은 수로 변경하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_WhenInvalidNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, false);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 0보다 작을 수 없습니다.");
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 때 주문할 수 없는 주문테이블이라면 예외를 반환한다.")
    @Test
    void changeNumberOfGuests_WhenEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(9))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 주문 테이블입니다.");
    }

    @DisplayName("주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(null, null, 0, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(false);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 때 주문할 수 없는 주문테이블이라면 예외를 반환한다.")
    @Test
    void changeEmpty_WhenHasTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(null, 1L, 0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹을 가지고 있을 경우 상태를 변경할 수 없습니다.");
    }
}

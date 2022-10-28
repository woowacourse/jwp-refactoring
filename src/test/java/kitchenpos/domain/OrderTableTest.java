package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문 가능한 상태로 변경한다.")
    void decideAvailabilityOfOrderRegistration() {
        OrderTable orderTable = new OrderTable(2, false);

        orderTable.decideAvailabilityOfOrderRegistration(OrderAble.POSSIBLE);

        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 그룹이 존재하면 예외를 발생한다.")
    void existTableGroup() {
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.decideAvailabilityOfOrderRegistration(OrderAble.IMPOSSIBLE))
                .withMessage("이미 단체지정이 되어있습니다.");
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(2, false);

        orderTable.changeNumberOfGuests(3);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("방문한 손님수가 0보다 낮을 경우 예외를 발생한다.")
    void changeInvalidGuestNumber() {
        OrderTable orderTable = new OrderTable(4, false);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .withMessage("방문한 손님 수는 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("빈 테이블에 방문한 손님수를 변경할 경우 예외를 발생한다.")
    void changeEmptyTable() {
        OrderTable orderTable = new OrderTable(1L, 1L, 3, true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(3))
                .withMessage("빈 테이블입니다.");
    }
}

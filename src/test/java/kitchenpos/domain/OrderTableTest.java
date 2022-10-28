package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    private static final int INVALID_NUMBER_OF_GUESTS = -1;

    @DisplayName("손님이 음수인 경우로 테이블을 생성할 수 없다.")
    @Test
    void createWithNegativeNumberOfGuests() {
        assertThatThrownBy(() -> OrderTable.of(INVALID_NUMBER_OF_GUESTS, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 비어있지 않은 상태로 변경할 수 있다.")
    @Test
    void changeTableNotEmpty() {
        // given
        final OrderTable orderTable = OrderTable.of(0, true);

        // when
        orderTable.changeEmptyStatus(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블을 비어있는 상태로 변경할 수 있다.")
    @Test
    void changeTableEmpty() {
        // given
        final OrderTable orderTable = OrderTable.of(0, false);

        // when
        orderTable.changeEmptyStatus(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 그룹(id)은 비어 있지 않으면 주문 테이블 상태를 변경할 수 없다.")
    @Test
    void canNotChangeEmptyWhenGroupIdNotNull() {
        // given
        final long tableGroupId = 1L;
        final OrderTable orderTable = new OrderTable(tableGroupId, 0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.changeEmptyStatus(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void canChangeGuestsCount() {
        // given
        final OrderTable orderTable = OrderTable.of(1, false);

        // when
        orderTable.updateNumberOfGuests(2);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("손님의 수는 0보다 작게 변경할 수 없다.")
    @Test
    void GuestsCountCanNotLessThenZero() {
        // given
        final OrderTable orderTable = OrderTable.of(1, false);

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(INVALID_NUMBER_OF_GUESTS))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경하려면 주문 테이블은 비어있으면 안된다.")
    @Test
    void tableEmptyWhenChangeGuestsCount() {
        // given
        final OrderTable orderTable = OrderTable.of(0, true);

        // when & then'
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

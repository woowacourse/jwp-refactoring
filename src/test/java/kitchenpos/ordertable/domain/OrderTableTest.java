package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("empty 상태를 변경한다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when
        orderTable.changeEmpty(false);

        // then
        assertThat(orderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("고객 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when
        orderTable.changeNumberOfGuests(1);

        // then
        assertThat(orderTable.numberOfGuests()).isOne();
    }

    @Test
    @DisplayName("고객 수를 변경할 때, 바꾸려는 고객 수가 0 미만이면 예외를 던진다.")
    void numberOfGuestsLessThanZeroException() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(-1));
    }

    @Test
    @DisplayName("고객 수를 변경할 때, 주문 테이블이 비어있으면 예외를 던진다.")
    void emptyOrderTableException() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTable.changeNumberOfGuests(1));
    }

    @Test
    @DisplayName("그룹을 해제한다.")
    void ungroup() {
        // given
        final OrderTable orderTable = new OrderTable(3, false);

        // when, then
        assertThatNoException()
                .isThrownBy(orderTable::ungroup);
    }
}

package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("생성자에서 방문한 손님 수가 음수이면 예외가 발생한다.")
    @Test
    void constructor_validateNumberOfGuests() {
        // given
        final int numberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("updateNumberOfGuests는 방문한 손님 수가 음수면 수정할 수 없다.")
    @Test
    void updateNumberOfGuests_validateNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(10, false);

        final int numberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("updateNumberOfGuests는 테이블이 비어있으면 수정할 수 없다.")
    @Test
    void updateNumberOfGuests_validateNotEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        final int numberOfGuests = 10;

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(numberOfGuests))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("updateEmpty는 단체 지정이 되어있으면 수정할 수 없다.")
    @Test
    void updateEmpty_validateNotGrouping() {
        // given
        final OrderTable orderTable = new OrderTable(null, 1L, 0, false);

        // when & then
        assertThatThrownBy(() -> orderTable.updateEmpty(true))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("numberOfGuests 값을 수정할 수 있다.")
    @Test
    void updateNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(10, false);

        final int numberOfGuests = 3;

        // when
        orderTable.updateNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("empty 값을 수정할 수 있다.")
    @Test
    void updateEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(10, false);

        // when
        orderTable.updateEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }
}

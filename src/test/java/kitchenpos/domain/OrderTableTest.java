package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 방문자_수를_변경한다() {
        // given
        final int numberOfGuests = 2;
        final OrderTable orderTable = new OrderTable(1, false);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    void 방문자_수를_변경할때_주문테이블이_비어있으면_예외가_발생한다() {
        // given
        final int numberOfGuests = 2;

        // when
        final OrderTable orderTable = new OrderTable(1, true);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 비어있으면 방문자 수를 변경할 수 없습니다.");
    }
}

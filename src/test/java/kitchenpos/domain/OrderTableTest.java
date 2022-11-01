package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void 주문_테이블의_상태를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        final OrderTable actual = orderTable.updateEmpty(true);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void 주문_테이블의_상태를_변경할때_테이블_그룹지정이_되어있는_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);

        // when, then
        assertThatThrownBy(() -> orderTable.updateEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        final OrderTable actual = orderTable.updateNumberOfGuests(2);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    void 주문_테이블의_손님_수를_변경할때_테이블이_빈자리인_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when, then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

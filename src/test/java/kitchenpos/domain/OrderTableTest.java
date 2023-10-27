package kitchenpos.domain;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTableTest {

    @Test
    void OrderTable을_생성한다() {
        // given, when
        final OrderTable orderTable = new OrderTable(0, false);

        // then
        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isZero(),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void OrderTable_생성_시_전달된_손님_수가_0_미만이면_예외가_발생한다() {
        // given
        final int numberOfGuests = -1;

        // when, then
        assertThatThrownBy(() -> new OrderTable(numberOfGuests, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_빈_상태를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        orderTable.changeEmpty(Boolean.TRUE);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void OrderTable의_빈_상태_변경시_null을_전달하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_빈_상태_변경시_OrderTable이_다른_테이블_그룹에_속해있다면_예외가_발생한다() {
        // given
        final TableGroup tableGroup = new TableGroup(1L);
        final OrderTable orderTable = new OrderTable(1L, tableGroup, 0, false);

        // when, then
        assertThatThrownBy(() -> orderTable.changeEmpty(Boolean.TRUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_손님_수를_변경한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        final Integer numberOfGuests = 10;
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void OrderTable의_손님_수_변경_시_null을_전달하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        final Integer numberOfGuests = null;

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_손님_수_변경_시_0보다_작은_수를_전달하면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);

        // when
        final Integer numberOfGuests = -1;

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void OrderTable의_손님_수_변경_시_OrderTable이_빈_상태면_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final Integer numberOfGuests = 10;

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
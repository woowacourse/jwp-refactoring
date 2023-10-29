package kitchenpos.domain;

import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.vo.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.tablegroup.TableGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("인원 수를 변경할 때 테이블이 착석 상태여야 한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        final NumberOfGuests numberOfGuestsBefore = new NumberOfGuests(3);
        final boolean empty = false;
        final OrderTable orderTable = new OrderTable(numberOfGuestsBefore, empty);

        // when
        final NumberOfGuests numberOfGuestsAfter = new NumberOfGuests(4);
        orderTable.changeNumberOfGuests(numberOfGuestsAfter);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuestsAfter);
    }

    @DisplayName("인원 수를 변경할 때 테이블이 빈 상태라면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_fail_when_empty() {
        // given
        final NumberOfGuests numberOfGuestsBefore = new NumberOfGuests(3);
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(numberOfGuestsBefore, empty);

        // when
        final NumberOfGuests numberOfGuestsAfter = new NumberOfGuests(4);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(numberOfGuestsAfter))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹이 없는 테이블은 착석 상태를 변경할 수 있다.")
    @Test
    void changeEmpty_success() {
        // given
        final NumberOfGuests numberOfGuestsBefore = new NumberOfGuests(3);
        final boolean empty = false;
        final OrderTable orderTable = new OrderTable(numberOfGuestsBefore, empty);

        // when
        final boolean expected = true;
        orderTable.changeEmpty(expected);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("그룹이 있는 테이블은 착석 상태를 변경할 시에 예외가 발생한다.")
    @Test
    void changeEmpty_fail_when_grouped() {
        // given
        final NumberOfGuests numberOfGuestsBefore = new NumberOfGuests(3);
        final OrderTable orderTable = new OrderTable(numberOfGuestsBefore, true);
        final TableGroup tableGroup = new TableGroup();
        orderTable.makeGroup(tableGroup);

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

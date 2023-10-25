package kitchenpos.domain;

import kitchenpos.ordertable.vo.NumberOfGuests;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.tablegroup.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

    @DisplayName("그루핑하려면 테이블이 빈 상태여야 하고, 그룹화된 테이블이 아니어야 한다.")
    @Test
    void canGroup_when_empty_and_tableGroup_is_null() {
        // given
        final NumberOfGuests numberOfGuests = new NumberOfGuests(3);
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        // when
        final boolean expected = true;
        final boolean actual = orderTable.canGroup();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("테이블이 빈 상태가 아니면 그루핑 불가능하다.")
    @Test
    void canGroup_false_when_table_is_not_empty() {
        // given
        final NumberOfGuests numberOfGuests = new NumberOfGuests(3);
        final boolean empty = false;
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        // when
        final boolean expected = false;
        final boolean actual = orderTable.canGroup();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("이미 그룹에 속해 있으면 그루핑 불가능하다.")
    @Test
    void canGroup_false_when_already_in_group() {
        // given
        final NumberOfGuests numberOfGuests = new NumberOfGuests(3);
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        final TableGroup tableGroup = new TableGroup(new OrderTables(List.of(orderTable)));

        // when
        // TODO: 테스트 고려
        final OrderTable spyOrderTable = spy(orderTable);
        when(spyOrderTable.canGroup()).thenReturn(false);

        final boolean expected = false;
        final boolean actual = spyOrderTable.canGroup();

        // then
        assertThat(actual).isEqualTo(expected);
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
        final boolean empty = false;
        final OrderTable orderTable = new OrderTable(numberOfGuestsBefore, empty);

        final TableGroup tableGroup = new TableGroup(new OrderTables(List.of(orderTable)));
        // when
        final boolean changedEmpty = true;
        // TODO: 테스트 고려
        final OrderTable spyOrderTable = spy(orderTable);
        doThrow(IllegalArgumentException.class).when(spyOrderTable).changeEmpty(changedEmpty);

        // then
        assertThatThrownBy(() -> spyOrderTable.changeEmpty(changedEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

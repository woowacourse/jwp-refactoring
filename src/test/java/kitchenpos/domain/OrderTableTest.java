package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.TableGroupFixtures;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void changeEmpty() {
        // given
        boolean empty = true;
        OrderTable orderTable = new OrderTable(2, false);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    void changeEmptyWithTableGroupId() {
        // given
        boolean empty = true;
        OrderTable orderTable = new OrderTable(1L, 2, false);
        // when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        int changeNumberOfGuests = 3;

        // when
        orderTable.changeNumberOfGuests(changeNumberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests().getCount()).isEqualTo(changeNumberOfGuests);
    }

    @Test
    void changeNumberOfGuestsWithEmpty() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        int changeNumberOfGuests = 3;

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        int changeNumberOfGuests = -1;

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(changeNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addRecord() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        // when
        orderTable.addRecord(1L, OrderStatus.COOKING);
        // then
        assertThat(orderTable.getRecords()).hasSize(1);
    }

    @Test
    void beGrouped() {
        // given
        boolean empty = true;
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        OrderTable orderTable = new OrderTable(0, empty);

        // when
        orderTable.group(tableGroup.getId());

        // then
        assertThat(orderTable.getTableGroupId()).isSameAs(tableGroup.getId());
        assertThat(orderTable.isEmpty()).isNotEqualTo(empty);
    }

    @Test
    void beGroupedWithNotEmpty() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();

        // when & then
        assertThatThrownBy(() -> orderTable.group(tableGroup.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void beGroupedWithTableGroupId() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable = new OrderTable(tableGroupId, 0, true);

        // when & then
        Long otherTableGroupId = 2L;
        assertThatThrownBy(() -> orderTable.group(otherTableGroupId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void beUngrouped() {
        // given
        OrderTable orderTable = new OrderTable(null, 2, false);
        orderTable.addRecord(1L, OrderStatus.COMPLETION);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    void beUngroupedWithNotCompleted() {
        // given
        OrderTable orderTable = new OrderTable(null, 2, false);
        OrderStatus orderStatus = OrderStatus.COOKING;
        orderTable.addRecord(1L, orderStatus);

        // when & then
        assertThatThrownBy(orderTable::ungroup)
                .isInstanceOf(IllegalStateException.class);
    }
}

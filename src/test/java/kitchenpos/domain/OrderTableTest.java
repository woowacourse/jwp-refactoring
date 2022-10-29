package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.OrderFixtures;
import kitchenpos.TableGroupFixtures;
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
        OrderTable orderTable = new OrderTable(TableGroupFixtures.createTableGroup(), 2, false);
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
    void addOrder() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        Order order = OrderFixtures.createOrder();
        // when
        orderTable.addOrder(order);
        // then
        assertThat(orderTable.getOrders()).hasSize(1);
    }

    @Test
    void beGrouped() {
        // given
        boolean empty = true;
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        OrderTable orderTable = new OrderTable(0, empty);

        // when
        orderTable.beGrouped(tableGroup);

        // then
        assertThat(orderTable.getTableGroup()).isSameAs(tableGroup);
        assertThat(orderTable.isEmpty()).isNotEqualTo(empty);
    }

    @Test
    void beGroupedWithNotEmpty() {
        // given
        OrderTable orderTable = new OrderTable(2, false);
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();

        // when & then
        assertThatThrownBy(() -> orderTable.beGrouped(tableGroup))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void beGroupedWithTableGroup() {
        // given
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        OrderTable orderTable = new OrderTable(tableGroup, 0, true);

        // when & then
        assertThatThrownBy(() -> orderTable.beGrouped(tableGroup))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void beUngrouped() {
        // given
        TableGroup tableGroup = TableGroupFixtures.createTableGroup();
        OrderTable orderTable = new OrderTable(tableGroup, 2, false);
        Order order = OrderFixtures.createOrder(OrderStatus.COMPLETION);
        orderTable.addOrder(order);

        // when
        orderTable.beUngrouped();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }

    @Test
    void beUngroupedWithNotCompleted() {
        // given
        OrderTable orderTable = new OrderTable(TableGroupFixtures.createTableGroup(), 2, false);
        OrderStatus orderStatus = OrderStatus.COOKING;
        Order order = OrderFixtures.createOrder(orderStatus);
        orderTable.addOrder(order);

        // when & then
        assertThatThrownBy(orderTable::beUngrouped)
                .isInstanceOf(IllegalStateException.class);
    }
}

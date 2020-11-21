package kitchenpos.ui;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableRestControllerTest extends ControllerTest {

    @Test
    void create() throws Exception {
        OrderTable table = table();
        assertAll(
                () -> assertThat(table.getId()).isEqualTo(1L),
                () -> assertThat(table.getTableGroupId()).isEqualTo(null),
                () -> assertThat(table.getNumberOfGuests()).isEqualTo(0),
                // 테이블을 추가할 때 null이 입력되므로 isEmpty()가 False로 설정되는 것이 맞는가?
                () -> assertThat(table.isEmpty()).isFalse(),
                () -> assertThat(table.getTableGroupId()).isNull()
        );
    }

    @Test
    void list() throws Exception {
        // given
        OrderTable table1 = table();
        OrderTable table2 = table();

        // when
        List<OrderTable> tables = tables();

        // then
        assertAll(
                () -> assertThat(tables).hasSize(2),
                () -> assertThat(tables.get(0).getId()).isEqualTo(table1.getId()),
                () -> assertThat(tables.get(1).getId()).isEqualTo(table2.getId())
        );
    }

    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTable table = table();

        // when
        table.setEmpty(true);
        OrderTable changedTable = changeEmpty(table);

        // then
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @Test
    void changeEmpty_orderStatusIsMealOrCooking() throws Exception {
        // given
        OrderTable table = table();
        OrderLineItem orderLineItem = orderLineItem();
        Order order = order(Collections.singletonList(orderLineItem), table);
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when
        table.setEmpty(true);
        assertThatThrownBy(() -> changeEmpty(table))
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_nonExisting() {
        // given
        OrderTable table = new OrderTable();
        table.setId(2L);

        // when
        table.setEmpty(true);
        assertThatThrownBy(() -> changeEmpty(table))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable table = table();

        // when
        table.setNumberOfGuests(3);
        OrderTable changedTable = changeGuests(table);

        // then
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(table.getNumberOfGuests());
    }

    @Test
    void changeNumberOfGuests_negative() throws Exception {
        // given
        OrderTable table = table();

        // when
        table.setNumberOfGuests(-1);
        assertThatThrownBy(() -> changeGuests(table))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_empty() throws Exception {
        // given
        OrderTable table = table();

        // when
        table.setEmpty(true);
        changeEmpty(table);
        table.setNumberOfGuests(3);
        assertThatThrownBy(() -> changeGuests(table))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_nonExisting() {
        // given
        OrderTable nonExistingTable = new OrderTable();
        nonExistingTable.setId(2L);

        // when
        assertThatThrownBy(() -> changeGuests(nonExistingTable))
                // then
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }
}
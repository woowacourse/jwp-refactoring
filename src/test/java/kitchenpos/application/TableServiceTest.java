package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableServiceTest extends IntegrationTest {

    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(4);
        assertThat(savedOrderTable.isEmpty()).isFalse();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @Test
    void list() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);
        newOrderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = tableService.create(newOrderTable);

        // when
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }
}
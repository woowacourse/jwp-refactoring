package kitchenpos.application.integration;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ApplicationIntegrationTest {

    @Test
    void create_table() {
        //given
        final int numberOfGuests = 0;
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        //when
        final OrderTable createdTable = tableService.create(orderTable);

        //then
        assertThat(createdTable)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @Test
    @Disabled
    void cannot_create_table_with_negative_number_of_guests() {
        //given
        final int numberOfGuests = -1;
        final boolean empty = true;
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        //when & then
        assertThatThrownBy(() -> tableService.create(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Disabled
    void cannot_create_table_with_invalid_table_group() {
        //given
        final long tableGroupId = 100L;
        final OrderTable orderTable = new OrderTable(tableGroupId, 0, true);
        //when & then
        assertThatThrownBy(() -> tableService.create(orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_change_empty_table_with_cooking_order_status() {
        //given
        final int numberOfGuests = -1;
        final boolean empty = false;
        final OrderTable createdTable = tableService.create(new OrderTable(numberOfGuests, empty));
        createOrder(createdTable.getId());

        final boolean changedEmpty = false;
        final OrderTable changedOrderTable = new OrderTable(changedEmpty);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(createdTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_change_empty_table_with_table_group() {
        //given
        final int numberOfGuests = -1;
        final boolean empty = true;
        final OrderTable createdTable1 = tableService.create(new OrderTable(numberOfGuests, empty));
        final OrderTable createdTable2 = tableService.create(new OrderTable(numberOfGuests, empty));
        List<OrderTable> orderTables = List.of(createdTable1, createdTable2);
        tableGroupService.create(new TableGroup(orderTables));

        final boolean changedEmpty = true;
        final OrderTable changedOrderTable = new OrderTable(changedEmpty);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(createdTable1.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void change_number_of_guests() {
        //given
        final int numberOfGuests = 0;
        final boolean empty = false;
        final OrderTable createdTable = tableService.create(new OrderTable(numberOfGuests, empty));
        final int changedNumberOfGuests = 1;
        final OrderTable changedOrderTable = new OrderTable(changedNumberOfGuests);

        //when
        final OrderTable changedTable = tableService.changeNumberOfGuests(createdTable.getId(), changedOrderTable);

        //then
        assertThat(changedTable)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(changedOrderTable);
    }

    @Test
    @Disabled
    void cannot_change_number_of_guests_with_negative_number_of_guests() {
        //given
        final int numberOfGuests = 0;
        final boolean empty = false;
        final OrderTable createdTable = tableService.create(new OrderTable(numberOfGuests, empty));
        final int changedNumberOfGuests = -1;
        final OrderTable changedOrderTable = new OrderTable(changedNumberOfGuests);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cannot_change_number_of_guests_with_empty_order_table() {
        //given
        final int numberOfGuests = 0;
        final boolean empty = true;
        final OrderTable createdTable = tableService.create(new OrderTable(numberOfGuests, empty));
        final int changedNumberOfGuests = 3;
        final OrderTable changedOrderTable = new OrderTable(changedNumberOfGuests);

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(createdTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
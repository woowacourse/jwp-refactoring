package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void create() {
        OrderTable orderTable = ORDER_TABLE_FIXTURE_1;

        OrderTable persistOrderTable = tableService.create(orderTable);

        assertThat(persistOrderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        tableService.create(ORDER_TABLE_FIXTURE_1);
        tableService.create(ORDER_TABLE_FIXTURE_2);

        List<OrderTable> orderTables = tableService.list();
        List<Integer> numberOfGuestsInOrderTables = orderTables.stream()
            .map(OrderTable::getNumberOfGuests)
            .collect(Collectors.toList());

        assertThat(numberOfGuestsInOrderTables)
            .contains(ORDER_TABLE_FIXTURE_1.getNumberOfGuests(), ORDER_TABLE_FIXTURE_2.getNumberOfGuests());
    }

    @Test
    void changeEmpty() {
        OrderTable persistOrderTable = tableService.create(ORDER_TABLE_FIXTURE_1);
        OrderTable requestOrderTable = new OrderTable();
        requestOrderTable.setEmpty(true);

        OrderTable changedOrderTable = tableService.changeEmpty(persistOrderTable.getId(), requestOrderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        OrderTable persistOrderTable = tableService.create(orderTable);
        OrderTable requestOrderTable = new OrderTable();
        requestOrderTable.setNumberOfGuests(100);

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(persistOrderTable.getId(), requestOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(100);
    }
}
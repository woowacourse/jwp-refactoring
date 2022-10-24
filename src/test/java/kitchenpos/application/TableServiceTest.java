package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable(2, false));

        assertThat(orderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        tableService.create(new OrderTable(2, false));

        assertThat(tableService.list()).hasSize(1);
    }

    @Test
    void changeEmpty() {
        OrderTable orderTable = tableService.create(new OrderTable(2, false));
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), new OrderTable(0, true));

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        final int numberOfGuests = 3;

        OrderTable orderTable = tableService.create(new OrderTable(2, false));
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(
                numberOfGuests, false));

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }
}

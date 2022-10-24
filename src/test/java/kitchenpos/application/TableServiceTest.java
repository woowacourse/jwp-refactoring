package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(orderTableDao.findById(savedOrderTable.getId())).isPresent();
    }

    @Test
    void list() {
        OrderTable orderTable = new OrderTable();

        int beforeSize = tableService.list().size();
        tableService.create(orderTable);

        assertThat(tableService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void changeEmpty_orderTableIdException() {
        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_notNullTableGroupIdException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable);

        assertThat(orderTableDao.findById(savedOrderTable.getId()).get().getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void changeNumberOfGuests_guestCountException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(-1);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_notFoundOrderTableIdException() {
        OrderTable orderTable = new OrderTable();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_emptyException() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

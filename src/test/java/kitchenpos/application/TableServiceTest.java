package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(value = "/truncate.sql")
@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Transactional
    @DisplayName("OrderTable을 추가할 수 있다.")
    @CsvSource(value = {"true, 0", "true, 1", "false, 0", "false, 2"})
    @ParameterizedTest
    void createTableTest(boolean emptyStatus, int numberOfGuests) {
        // TODO: 2020/10/22 레거시 리팩토링 할 때, isEmpty: true, numberOfGuest: 1이상 조건으로 테이블이 생성되지 않도록 막아야 할 듯?
        OrderTable orderTable = createTable(emptyStatus, numberOfGuests);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(() -> {
            assertThat(savedOrderTable).isNotNull();
            assertThat(savedOrderTable.getId()).isNotNegative();
            assertThat(savedOrderTable.getTableGroupId()).isNull();
            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
        });
    }

    @Transactional
    @DisplayName("전체 OrderTable을 조회할 수 있다.")
    @Test
    void findAllTablesTest() {
        OrderTable table1 = createOccupiedTable();
        OrderTable table2 = createEmptyTable();
        OrderTable table3 = createEmptyTable();

        orderTableDao.save(table1);
        orderTableDao.save(table2);
        orderTableDao.save(table3);

        List<OrderTable> orderTables = tableService.list();

        assertAll(() -> {
            assertThat(orderTables).hasSize(3);
            assertThat(orderTables).extracting(OrderTable::getId)
                    .hasSize(3)
                    .allMatch(id -> id > 0);
        });
    }

    @Transactional
    @DisplayName("OrderTable의 사용 상태를 수정할 수 있다.")
    @CsvSource(value = {"true, false", "true, true", "false, true", "false, false"})
    @ParameterizedTest
    void changeEmptyStatusOfTableTest(boolean previousStatus, boolean stateToChange) {
        OrderTable emptyTable = createTable(previousStatus, 0);
        OrderTable savedTable = orderTableDao.save(emptyTable);

        OrderTable orderTable = createTableToChange(stateToChange);

        OrderTable savedOrderTable = tableService.changeEmpty(savedTable.getId(), orderTable);

        assertThat(savedOrderTable.isEmpty()).isEqualTo(stateToChange);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable의 사용 상태를 수정")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeEmptyStatusOfInvalidTableTest(boolean emptyStatus) {
        OrderTable orderTable = createTableToChange(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(100L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 테이블 그룹에 속한 OrderTable의 사용 상태를 수정")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeEmptyStatusOfGroupedTableTest(boolean emptyStatus) {
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable groupedTable = createGroupedTable(groupId);
        OrderTable savedTable = orderTableDao.save(groupedTable);

        OrderTable orderTable = createTableToChange(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: OrderStatus가 COOKING 혹은 MEAL인 OrderTable의 사용 상태 수정")
    @CsvSource(value = {"COOKING, true", "COOKING, false", "MEAL, true", "MEAL, false"})
    @ParameterizedTest
    void changeEmptyStatusOfTableHavingNotProperStatusTest(OrderStatus status, boolean emptyStatus) {
        OrderTable emptyTable = createEmptyTable();
        OrderTable savedTable = orderTableDao.save(emptyTable);

        Order order = createOrder(savedTable.getId());
        order.setOrderStatus(status.name());
        orderDao.save(order);

        OrderTable orderTable = createTableToChange(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("OrderTable의 손님 수를 수정할 수 있다.")
    @ValueSource(ints = {0, 1, 2})
    @ParameterizedTest
    void changeNumberOfGuestsTest(int numberOfGuests) {
        OrderTable occupiedTable = createOccupiedTable();
        OrderTable savedTable = orderTableDao.save(occupiedTable);

        OrderTable orderTable = createTableToChange(numberOfGuests);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(savedTable.getId(), orderTable);

        assertAll(() -> {
            assertThat(savedOrderTable).isNotNull();
            assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
        });
    }

    @Transactional
    @DisplayName("예외: OrderTable의 손님 수를 0명 이하로 수정")
    @ValueSource(ints = {-1, -2})
    @ParameterizedTest
    void changeNumberOfGuestsUnderZeroTest(int numberOfGuests) {
        OrderTable occupiedTable = createOccupiedTable();
        OrderTable savedTable = orderTableDao.save(occupiedTable);

        OrderTable orderTable = createTableToChange(numberOfGuests);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable의 손님수를 수정")
    @Test
    void changeNumberOfGuestsOfInvalidTableTest() {
        OrderTable orderTable = createTableToChange(5);

        assertThatThrownBy(() -> tableService.changeEmpty(100L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 비어있는 OrderTable에 손님수를 수정")
    @Test
    void changeNumberOfGuestsOfEmptyTableTest() {
        OrderTable emptyTable = createEmptyTable();
        OrderTable savedEmptyTable = orderTableDao.save(emptyTable);

        OrderTable orderTable = createTableToChange(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedEmptyTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
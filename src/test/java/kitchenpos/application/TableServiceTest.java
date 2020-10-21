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

import static kitchenpos.application.TableGroupServiceTest.createOrder;
import static kitchenpos.application.TableGroupServiceTest.createTableGroup;
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
    void createTable(boolean emptyStatus, int numberOfGuests) {
        OrderTable orderTable = createOrderTable(emptyStatus, numberOfGuests);

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
    void findAllTables() {
        orderTableDao.save(new OrderTable());
        orderTableDao.save(new OrderTable());
        orderTableDao.save(new OrderTable());

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
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeEmptyStatusOfTable(boolean emptyStatus) {
        OrderTable savedTable = orderTableDao.save(new OrderTable());
        OrderTable orderTable = createOrderTable(emptyStatus);

        OrderTable savedOrderTable = tableService.changeEmpty(savedTable.getId(), orderTable);

        assertThat(savedOrderTable.isEmpty()).isEqualTo(emptyStatus);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable의 사용 상태를 수정")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeEmptyStatusOfInvalidTable(boolean emptyStatus) {
        OrderTable orderTable = createOrderTable(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(100L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 테이블 그룹에 속한 OrderTable의 사용 상태를 수정")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeEmptyStatusOfGroupedTable(boolean emptyStatus) {
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable groupedTable = createOrderTable(groupId, true, 0);
        OrderTable savedTable = orderTableDao.save(groupedTable);

        OrderTable orderTable = createOrderTable(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: OrderStatus가 COOKING 혹은 MEAL인 OrderTable의 사용 상태 수정")
    @CsvSource(value = {"COOKING, true", "COOKING, false", "MEAL, true", "MEAL, false"})
    @ParameterizedTest
    void changeEmptyStatusOfTableHavingNotProperStatus(OrderStatus status, boolean emptyStatus) {
        OrderTable savedTable = orderTableDao.save(new OrderTable());
        Order order = createOrder(savedTable.getId(), status);
        orderDao.save(order);

        OrderTable orderTable = createOrderTable(true);
        orderTable.setEmpty(emptyStatus);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("OrderTable의 손님 수를 수정할 수 있다.")
    @ValueSource(ints = {0, 1, 2})
    @ParameterizedTest
    void changeNumberOfGuests(int numberOfGuests) {
        OrderTable occupiedTable = createOrderTable(false);
        OrderTable savedTable = orderTableDao.save(occupiedTable);

        OrderTable orderTable = createOrderTable(numberOfGuests);

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
    void changeNumberOfGuestsUnderZero(int numberOfGuests) {
        OrderTable occupiedTable = createOrderTable(false);
        OrderTable savedTable = orderTableDao.save(occupiedTable);

        OrderTable orderTable = createOrderTable(numberOfGuests);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable의 손님수를 수정")
    @Test
    void changeNumberOfGuestsOfInvalidTable() {
        OrderTable orderTable = createOrderTable(5);

        assertThatThrownBy(() -> tableService.changeEmpty(100L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 비어있는 OrderTable에 손님수를 수정")
    @Test
    void changeNumberOfGuestsOfEmptyTable() {
        OrderTable emptyTable = createOrderTable(true);
        OrderTable savedEmptyTable = orderTableDao.save(emptyTable);

        OrderTable orderTable = createOrderTable(5);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedEmptyTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(Long groupId, boolean emptyStatus, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(groupId);
        orderTable.setEmpty(emptyStatus);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    private OrderTable createOrderTable(boolean emptyStatus, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    static OrderTable createOrderTable(boolean emptyStatus) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        return orderTable;
    }

    private OrderTable createOrderTable(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
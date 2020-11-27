package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupDao tableGroupDao;
    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTableRequest = createOrderTable(null, 1L, 2, false);

        OrderTable saved = tableService.create(orderTableRequest);

        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void list() {
        TableGroup tableGroup = tableGroupDao.save(
            createTableGroup(null, LocalDateTime.now(), new ArrayList<>()));
        orderTableDao.save(createOrderTable(null, tableGroup.getId(), 2, false));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable table = tableService.create(
            createOrderTable(null, null, 2, false));
        OrderTable emptyTableRequest = createOrderTable(table.getId(),
            table.getTableGroupId(), table.getNumberOfGuests(), true);

        OrderTable changedOrderTable = tableService.changeEmpty(emptyTableRequest.getId(),
            emptyTableRequest);

        assertAll(
            () -> assertThat(table.isEmpty()).isFalse(),
            () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("빈 테이블로 변경 시, 단체 지정된 주문 테이블은 빈 테이블로 설정할 수 없다.")
    @Test
    void changeEmpty_WithTableGroup_ThrownException() {
        TableGroup tableGroup = tableGroupDao.save(
            createTableGroup(null, LocalDateTime.now(), new ArrayList<>()));
        OrderTable table = orderTableDao.save(createOrderTable(null, tableGroup.getId(), 0, false));
        OrderTable emptyTableRequest = createOrderTable(table.getId(),
            table.getTableGroupId(), table.getNumberOfGuests(), true);

        assertThatThrownBy(() -> tableService.changeEmpty(emptyTableRequest.getId(),
            emptyTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경 시, 주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블로 설정할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_WithInvalidOrderState_ThrownException(String status) {
        OrderTable table = orderTableDao.save(createOrderTable(null, null, 0, false));
        orderDao.save(
            createOrder(null, table.getId(), status, LocalDateTime.now(), new ArrayList<>()));
        OrderTable emptyTableRequest = createOrderTable(table.getId(),
            table.getTableGroupId(), table.getNumberOfGuests(), true);

        assertThatThrownBy(() -> tableService.changeEmpty(emptyTableRequest.getId(),
            emptyTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        int numberOfGuests = 3;
        OrderTable orderTable = orderTableDao.save(
            createOrderTable(null, null, 1, false));
        OrderTable changedOrderTableRequest = createOrderTable(orderTable.getId(),
            orderTable.getTableGroupId(), numberOfGuests, false);

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(
            changedOrderTableRequest.getId(), changedOrderTableRequest);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("손님 수 변경 시, 음수면 예외가 발생한다")
    @Test
    void changeNumberOfGuests_WithNegativeNumber_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 1, false));
        OrderTable changedOrderTableRequest = createOrderTable(orderTable.getId(),
            orderTable.getTableGroupId(), -3, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(changedOrderTableRequest.getId(),
            changedOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경 시, 빈 테이블이면 예외가 발생한다")
    @Test
    void changeNumberOfGuests_WithEmptyTable_ThrownException() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable changedOrderTableRequest = createOrderTable(orderTable.getId(),
            orderTable.getTableGroupId(), 3, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(changedOrderTableRequest.getId(),
            changedOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
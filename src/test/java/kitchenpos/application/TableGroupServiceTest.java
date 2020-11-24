package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(orderTable1, orderTable2));

        TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        assertAll(
            () -> assertThat(tableGroup.getId()).isNotNull(),
            () -> assertThat(tableGroup.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("단체 지정 시, 비어있지 않은 테이블은 단체지정을 할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void create_WithNotEmptyTable_ThrownException(Long tableId) {
        OrderTable orderTable1 = orderTableDao.save(
            createOrderTable(null, null, tableId.equals(1L) ? 0 : 2, tableId.equals(1L)));
        OrderTable orderTable2 = orderTableDao.save(
            createOrderTable(null, null, tableId.equals(2L) ? 0 : 2, tableId.equals(2L)));
        TableGroup tableGroupRequest = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 2개 미만의 테이블은 단체지정을 할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void create_WithLessThanTwoTables_ThrownException(int size) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            OrderTable orderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
            orderTables.add(orderTable);
        }
        TableGroup tableGroupRequest = createTableGroup(null, LocalDateTime.now(), orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 존재하지 않는 테이블은 단체지정 할 수 없다.")
    @Test
    void create_WithNonExistingTable_ThrownException() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable notSavedTable = createOrderTable(null, null, 2, true);
        TableGroup tableGroupRequest = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedTable, notSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 이미 단체 지정된 테이블은 지정할 수 없다.")
    @Test
    void create_WithTableAlreadyInTableGroup_ThrownException() {
        OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable3 = orderTableDao.save(createOrderTable(null, null, 0, true));
        tableGroupService.create(createTableGroup(null, LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));
        TableGroup tableGroupRequest = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(orderTable2, orderTable3));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
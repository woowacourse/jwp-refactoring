package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupCreateRequestDto;
import kitchenpos.dto.TableGroupResponseDto;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroupCreateRequestDto tableGroupCreateRequest = new TableGroupCreateRequestDto(
            Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        TableGroupResponseDto tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

        assertAll(
            () -> assertThat(tableGroupResponse.getId()).isNotNull(),
            () -> assertThat(tableGroupResponse.getOrderTables()).hasSize(2)
        );
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
        List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
        TableGroupCreateRequestDto tableGroupCreateRequest = new TableGroupCreateRequestDto(orderTableIds);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시, 존재하지 않는 테이블은 단체지정 할 수 없다.")
    @Test
    void create_WithNonExistingTable_ThrownException() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable notSavedTable = createOrderTable(null, null, 2, true);
        TableGroupCreateRequestDto tableGroupCreateRequest = new TableGroupCreateRequestDto(
            Arrays.asList(savedTable.getId(), notSavedTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroupResponseDto tableGroupResponse = tableGroupService.create(
            new TableGroupCreateRequestDto(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        tableGroupService.ungroup(tableGroupResponse.getId());

        for (OrderTable orderTable : orderTableDao.findAll()) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }

    @DisplayName("단체 해제 시, 단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_WithInvalidOrderStatus_ThrownException(String status) {
        OrderTable orderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroupResponseDto tableGroupResponse = tableGroupService.create(
            new TableGroupCreateRequestDto(Arrays.asList(orderTable1.getId(), orderTable2.getId())));
        orderDao.save(new Order(null, orderTable1.getId(), status, LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
package kitchenpos.tablegroup.application;

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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.ServiceTest;
import kitchenpos.order.model.Order;
import kitchenpos.order.model.OrderStatus;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequestDto;
import kitchenpos.tablegroup.application.dto.TableGroupResponseDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;

class TableGroupServiceTest extends ServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, null, 0, true));
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
            OrderTable orderTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
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
        OrderTable savedTable = orderTableRepository.save(new OrderTable(null, null, 0, true));
        OrderTable notSavedTable = new OrderTable(null, null, 2, true);
        TableGroupCreateRequestDto tableGroupCreateRequest = new TableGroupCreateRequestDto(
            Arrays.asList(savedTable.getId(), notSavedTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해지할 수 있다.")
    @Test
    void ungroup() {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, null, 0, true));
        TableGroupResponseDto tableGroupResponse = tableGroupService.create(
            new TableGroupCreateRequestDto(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        tableGroupService.ungroup(tableGroupResponse.getId());

        for (OrderTable orderTable : orderTableRepository.findAll()) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }

    @DisplayName("단체 해제 시, 단체 지정된 주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해지할 수 없다.")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void ungroup_WithInvalidOrderStatus_ThrownException(OrderStatus status) {
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, null, 0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, null, 0, true));
        TableGroupResponseDto tableGroupResponse = tableGroupService.create(
            new TableGroupCreateRequestDto(Arrays.asList(orderTable1.getId(), orderTable2.getId())));
        orderRepository.save(new Order(null, orderTable1.getId(), status, LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
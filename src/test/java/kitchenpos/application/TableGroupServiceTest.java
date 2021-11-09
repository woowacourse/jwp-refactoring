package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.service.TableGroupRequest;
import kitchenpos.table.service.TableGroupRequest.OrderTableId;
import kitchenpos.table.service.TableGroupResponse;
import kitchenpos.table.service.TableGroupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTestWithProfiles
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    private OrderTable table1;
    private OrderTable table2;
    private OrderTableId tableId1;
    private OrderTableId tableId2;
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    void setUp() {
        table1 = orderTableRepository.save(new OrderTable(3, true));
        table2 = orderTableRepository.save(new OrderTable(3, true));

        tableId1 = new OrderTableId(table1.getId());
        tableId2 = new OrderTableId(table2.getId());
        tableGroupRequest = new TableGroupRequest(Arrays.asList(tableId1, tableId2));
    }

    @Test
    @DisplayName("테이블 단체 정상 생성")
    void create() {
        TableGroupResponse saved = tableGroupService.create(tableGroupRequest);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedDate());
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 0개 테이블")
    void createWithEmptyTable() {
        TableGroupRequest groupRequest = new TableGroupRequest(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(groupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 1개 테이블")
    void createWithSingleTable() {
        TableGroupRequest groupRequest = new TableGroupRequest(Collections.singletonList(tableId1));

        assertThatThrownBy(() -> tableGroupService.create(groupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 존재하지 않는 테이블 포함")
    void createContainingNotExistingTable() {
        OrderTableId notExistingTableId = new OrderTableId(Long.MAX_VALUE);
        TableGroupRequest groupRequest = new TableGroupRequest(Arrays.asList(tableId1, notExistingTableId));

        assertThatThrownBy(() -> tableGroupService.create(groupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 빈 상태가 아닌 테이블 포함")
    void createContainingNotEmptyTable() {
        OrderTable notEmptyTable = orderTableRepository.save(new OrderTable(3, false));
        OrderTableId notEmptyTableId = new OrderTableId(notEmptyTable.getId());

        TableGroupRequest groupRequest = new TableGroupRequest(Arrays.asList(tableId1, notEmptyTableId));

        assertThatThrownBy(() -> tableGroupService.create(groupRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 이미 단체 지정된 테이블 포함")
    void createContainingGroupedTable() {
        tableGroupService.create(tableGroupRequest);

        OrderTable newTable = orderTableRepository.save(new OrderTable(3, true));
        OrderTableId newTableId = new OrderTableId(newTable.getId());

        TableGroupRequest newRequest = new TableGroupRequest(Arrays.asList(tableId1, newTableId));

        assertThatThrownBy(() -> tableGroupService.create(newRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 정상 취소")
    void ungroup() {
        TableGroupResponse tableGroup = tableGroupService.create(tableGroupRequest);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = orderTableRepository.findAllByIdIn(
                Arrays.asList(table1.getId(), table2.getId()));
        assertThat(tables)
                .allMatch(table -> Objects.isNull(table.getTableGroup()))
                .allMatch(table -> !table.isEmpty());
    }

    @ParameterizedTest(name = "단체 지정 취소 실패 :: 주문 상태 {0} 테이블 포함")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroupTableGroupOfTableWithNotAllowedOrderStatus(OrderStatus orderStatus) {
        TableGroupResponse tableGroup = tableGroupService.create(tableGroupRequest);

        orderRepository.save(
                new Order(table1, orderStatus.name(), Collections.emptyList()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
        tableGroupRepository.deleteAllInBatch();
    }
}

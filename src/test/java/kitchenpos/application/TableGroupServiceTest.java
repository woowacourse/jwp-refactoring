package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.SpringBootTestWithProfiles;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
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

    @Test
    @DisplayName("테이블 단체 정상 생성")
    void create() {
        OrderTable table1 = orderTableRepository.save(new OrderTable(true));
        OrderTable table2 = orderTableRepository.save(new OrderTable(true));

        TableGroup tableGroup = new TableGroup(Arrays.asList(table1, table2));
        TableGroup saved = tableGroupService.create(tableGroup);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedDate());
        assertThat(saved.getOrderTables())
                .hasSize(2)
                .allMatch(table -> !table.isEmpty())
                .allMatch(table -> Objects.equals(table.getTableGroupId(), saved.getId()));
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 0개 테이블")
    void createWithEmptyTable() {
        TableGroup tableGroup = new TableGroup(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 1개 테이블")
    void createWithSingleTable() {
        OrderTable table = orderTableRepository.save(new OrderTable(true));
        TableGroup tableGroup = new TableGroup(Collections.singletonList(table));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 존재하지 않는 테이블 포함")
    void createContainingNotExistingTable() {
        OrderTable table = orderTableRepository.save(new OrderTable(true));
        OrderTable notSavedOrderTable = new OrderTable(false);

        TableGroup tableGroup = new TableGroup(Arrays.asList(table, notSavedOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 빈 상태가 아닌 테이블 포함")
    void createContainingNotEmptyTable() {
        OrderTable table = orderTableRepository.save(new OrderTable(true));
        OrderTable notEmptyTable = orderTableRepository.save(new OrderTable(false));

        TableGroup tableGroup = new TableGroup(Arrays.asList(table, notEmptyTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 생성 실패 :: 이미 단체 지정된 테이블 포함")
    void createContainingGroupedTable() {
        OrderTable groupedTable1 = orderTableRepository.save(new OrderTable(true));
        OrderTable groupedTable2 = orderTableRepository.save(new OrderTable(true));

        tableGroupService.create(new TableGroup(Arrays.asList(groupedTable1, groupedTable2)));

        OrderTable table = orderTableRepository.save(new OrderTable(true));

        TableGroup tableGroup = new TableGroup(Arrays.asList(table, groupedTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 정상 취소")
    void ungroup() {
        OrderTable groupedTable1 = orderTableRepository.save(new OrderTable(true));
        OrderTable groupedTable2 = orderTableRepository.save(new OrderTable(true));

        TableGroup tableGroup = tableGroupService.create(new TableGroup(Arrays.asList(groupedTable1, groupedTable2)));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = orderTableRepository.findAllByIdIn(
                Arrays.asList(groupedTable1.getId(), groupedTable2.getId()));
        assertThat(tables)
                .allMatch(table -> Objects.isNull(table.getTableGroupId()))
                .allMatch(table -> !table.isEmpty());
    }

    @ParameterizedTest(name = "단체 지정 취소 실패 :: 주문 상태 {0} 테이블 포함")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroupTableGroupOfTableWithNotAllowedOrderStatus(OrderStatus orderStatus) {
        OrderTable table1 = orderTableRepository.save(new OrderTable(true));
        OrderTable table2 = orderTableRepository.save(new OrderTable(true));

        TableGroup tableGroup = tableGroupService.create(new TableGroup(Arrays.asList(table1, table2)));

        orderRepository.save(
                new Order(table1.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));

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

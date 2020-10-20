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
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;


    @Transactional
    @DisplayName("Table Group을 추가할 수 있다.")
    @Test
    void groupTables() {
        OrderTable firstTable = createEmptyTable(1L);
        OrderTable secondTable = createEmptyTable(2L);
        TableGroup tableGroup = createTableGroup(firstTable, secondTable);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup).isNotNull();
            List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();
            assertThat(savedOrderTables).hasSize(2);
            assertThat(savedOrderTables).extracting(OrderTable::getId).containsOnly(firstTable.getId(), secondTable.getId());
            assertThat(savedOrderTables).extracting(OrderTable::getTableGroupId).containsOnly(savedTableGroup.getId());
            assertThat(savedOrderTables).extracting(OrderTable::isEmpty).containsOnly(false);
        });
    }

    @DisplayName("예외: OrderTables 없이 TableGroup 을 생성")
    @Test
    void groupTablesWithoutOrderTables() {
        TableGroup tableGroup = createTableGroup();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 한 개의 OrderTable 로 TableGroup 생성 (2개 이상의 OrderTable가 요구됨)")
    @Test
    void groupTablesWithOneOrderTable() {
        OrderTable firstTable = createEmptyTable(1L);
        TableGroup tableGroup = createTableGroup(firstTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable 로 TableGroup 생성")
    @Test
    void groupTablesWithInvalidOrderTable() {
        OrderTable firstTable = createEmptyTable(1L);
        OrderTable invalidTable = createEmptyTable(100L);
        TableGroup tableGroup = createTableGroup(firstTable, invalidTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 사용중인 OrderTable로 TableGroup 생성")
    @Test
    void groupTablesWithOccupiedOrderTable() {
        OrderTable occupiedTable = createOccupiedTable(3L);
        orderTableDao.save(occupiedTable);
        OrderTable emptyTable = createEmptyTable(1L);
        TableGroup tableGroup = createTableGroup(occupiedTable, emptyTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("예외: 이미 TableGroup 이 있는 OrderTable 로 TableGroup 생성")
    @Test
    void groupTablesWithAlreadyGroupedTable() {
        TableGroup existingGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(existingGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable groupedTable = createGroupedTable(4L, groupId);
        orderTableDao.save(groupedTable);
        OrderTable emptyTable = createEmptyTable(1L);
        TableGroup tableGroup = createTableGroup(groupedTable, emptyTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("테이블 그룹을 해제 할 수 있다.")
    @Test
    void ungroupTables() {
        TableGroup existingGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(existingGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable firstTable = createGroupedTable(1L, groupId);
        orderTableDao.save(firstTable);
        OrderTable secondTable = createGroupedTable(2L, groupId);
        orderTableDao.save(secondTable);

        tableGroupService.ungroup(groupId);

        assertThat(orderTableDao.findAllByTableGroupId(groupId)).isEmpty();
    }

    @Transactional
    @DisplayName("예외: OrderStatus가 COOKING 혹은 MEAL인 OrderTable의 그룹 해제")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void ungroupTablesHavingNotProperOrderStatus(OrderStatus status) {
        TableGroup existingGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(existingGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable firstTable = createGroupedTable(1L, groupId);
        orderTableDao.save(firstTable);
        OrderTable secondTable = createGroupedTable(2L, groupId);
        orderTableDao.save(secondTable);

        Order order = createOrder(1L, status);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(groupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createEmptyTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);

        return orderTable;
    }

    private OrderTable createOccupiedTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(false);

        return orderTable;
    }

    private OrderTable createGroupedTable(Long id, Long groupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(groupId);

        return orderTable;
    }

    private TableGroup createTableGroup(OrderTable... tables) {
        List<OrderTable> orderTables = Arrays.asList(tables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());

        return tableGroup;
    }

    private Order createOrder(Long tableId, OrderStatus status) {
        Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderStatus(status.name());
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }
}
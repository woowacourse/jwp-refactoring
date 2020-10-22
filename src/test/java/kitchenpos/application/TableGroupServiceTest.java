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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@Transactional
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

    @DisplayName("Table Group을 추가할 수 있다.")
    @Test
    void groupTablesTest() {
        OrderTable firstTable = createEmptyTable();
        OrderTable savedFirstTable = orderTableDao.save(firstTable);

        OrderTable secondTable = createEmptyTable();
        OrderTable savedSecondTable = orderTableDao.save(secondTable);

        TableGroup tableGroup = createTableGroup(savedFirstTable, savedSecondTable);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup).isNotNull();
            List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();
            assertThat(savedOrderTables).hasSize(2);
            assertThat(savedOrderTables).extracting(OrderTable::getId).containsOnly(savedFirstTable.getId(), savedSecondTable.getId());
            assertThat(savedOrderTables).extracting(OrderTable::getTableGroupId).containsOnly(savedTableGroup.getId());
            assertThat(savedOrderTables).extracting(OrderTable::isEmpty).containsOnly(false);
        });
    }

    @DisplayName("예외: OrderTables 없이 TableGroup 을 생성")
    @Test
    void groupTablesWithoutOrderTablesTest() {
        TableGroup tableGroup = createTableGroup();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 한 개의 OrderTable 로 TableGroup 생성 (2개 이상의 OrderTable가 요구됨)")
    @Test
    void groupTablesWithOneOrderTableTest() {
        OrderTable firstTable = createEmptyTable();
        OrderTable savedFirstTable = orderTableDao.save(firstTable);

        TableGroup tableGroup = createTableGroup(savedFirstTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 존재하지 않는 OrderTable 로 TableGroup 생성")
    @Test
    void groupTablesWithInvalidOrderTableTest() {
        OrderTable firstTable = createEmptyTable();
        OrderTable savedFirstTable = orderTableDao.save(firstTable);

        OrderTable invalidTable = createEmptyTable();
        invalidTable.setId(200L);

        TableGroup tableGroup = createTableGroup(savedFirstTable, invalidTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 사용중인 OrderTable로 TableGroup 생성")
    @Test
    void groupTablesWithOccupiedOrderTableTest() {
        OrderTable occupiedTable = createOccupiedTable();
        OrderTable savedOccupiedTable = orderTableDao.save(occupiedTable);

        OrderTable emptyTable = createEmptyTable();
        OrderTable savedEmptyTable = orderTableDao.save(emptyTable);

        TableGroup tableGroup = createTableGroup(savedOccupiedTable, savedEmptyTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 이미 TableGroup 이 있는 OrderTable 로 TableGroup 생성")
    @Test
    void groupTablesWithAlreadyGroupedTableTest() {
        TableGroup existingGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(existingGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable groupedTable = createGroupedTable(groupId);
        OrderTable savedGroupedTable = orderTableDao.save(groupedTable);

        OrderTable emptyTable = createEmptyTable();
        OrderTable savedEmptyTable = orderTableDao.save(emptyTable);

        TableGroup tableGroup = createTableGroup(savedGroupedTable, savedEmptyTable);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제 할 수 있다.")
    @Test
    void ungroupTablesTest() {
        TableGroup existingGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(existingGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable firstTable = createGroupedTable(groupId);
        orderTableDao.save(firstTable);

        OrderTable secondTable = createGroupedTable(groupId);
        orderTableDao.save(secondTable);

        tableGroupService.ungroup(groupId);

        assertThat(orderTableDao.findAllByTableGroupId(groupId)).isEmpty();
    }

    @DisplayName("예외: OrderStatus가 COOKING 혹은 MEAL인 OrderTable의 그룹 해제")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void ungroupTablesHavingNotProperOrderStatusTest(OrderStatus status) {
        TableGroup tableGroup = createTableGroup();
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        Long groupId = savedTableGroup.getId();

        OrderTable firstTable = createGroupedTable(groupId);
        OrderTable savedFirstTable = orderTableDao.save(firstTable);

        OrderTable secondTable = createGroupedTable(groupId);
        orderTableDao.save(secondTable);

        Order order = createOrder(savedFirstTable.getId());
        order.setOrderStatus(status.name());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(groupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
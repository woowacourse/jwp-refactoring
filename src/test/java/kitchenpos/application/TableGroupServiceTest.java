package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao tableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        OrderTable savedTable1 = tableDao.save(createTestTable(true));
        OrderTable savedTable2 = tableDao.save(createTestTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTestTableGroup(tables);
        TableGroup create = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(create.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NoTable() {
        OrderTable savedTable1 = tableDao.save(createTestTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1);

        TableGroup tableGroup = createTestTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable savedTable = tableDao.save(createTestTable(true));
        OrderTable notSavedTable = createTestTable(true);
        List<OrderTable> tables = Arrays.asList(savedTable, notSavedTable);

        TableGroup tableGroup = createTestTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블이 아닌 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotEmptyTable() {
        OrderTable emptyTable = tableDao.save(createTestTable(true));
        OrderTable notEmptyTable = tableDao.save(createTestTable(false));
        List<OrderTable> tables = Arrays.asList(emptyTable, notEmptyTable);

        TableGroup tableGroup = createTestTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        OrderTable savedTable1 = tableDao.save(createTestTable(true));
        OrderTable savedTable2 = tableDao.save(createTestTable(true));
        List<OrderTable> tables1 = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup1 = createTestTableGroup(tables1);
        tableGroupService.create(tableGroup1);

        OrderTable savedTable3 = tableDao.save(createTestTable(true));
        List<OrderTable> tables2 = Arrays.asList(savedTable2, savedTable3);

        TableGroup tableGroup2 = createTestTableGroup(tables2);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable savedTable1 = tableDao.save(createTestTable(true));
        OrderTable savedTable2 = tableDao.save(createTestTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTestTableGroup(tables);
        TableGroup create = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(create.getId());

        OrderTable ungroupedTable1 = tableDao.findById(savedTable1.getId()).get();
        OrderTable ungroupedTable2 = tableDao.findById(savedTable2.getId()).get();
        assertAll(
            () -> assertThat(ungroupedTable1.getTableGroupId()).isNull(),
            () -> assertThat(ungroupedTable2.getTableGroupId()).isNull(),
            () -> assertThat(ungroupedTable1.isEmpty()).isFalse(),
            () -> assertThat(ungroupedTable2.isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
    @Test
    void ungroup_Fail_With_TableInProgress() {
        OrderTable savedTable1 = tableDao.save(createTestTable(true));
        OrderTable savedTable2 = tableDao.save(createTestTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTestTableGroup(tables);
        TableGroup create = tableGroupService.create(tableGroup);

        Order order = createTestOrder(savedTable1);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(create.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createTestTableGroup(List<OrderTable> tables) {
        return TableGroup.builder()
            .orderTables(tables)
            .createdDate(LocalDateTime.now())
            .build();
    }

    private OrderTable createTestTable(boolean empty) {
        return OrderTable.builder()
            .empty(empty)
            .build();
    }

    private Order createTestOrder(OrderTable table) {
        return Order.builder()
            .orderTableId(table.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
    }
}
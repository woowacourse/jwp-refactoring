package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createTable;
import static kitchenpos.TestObjectFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
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
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTableGroup(tables);
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
        OrderTable savedTable1 = tableDao.save(createTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1);

        TableGroup tableGroup = createTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable savedTable = tableDao.save(createTable(true));
        OrderTable notSavedTable = createTable(true);
        List<OrderTable> tables = Arrays.asList(savedTable, notSavedTable);

        TableGroup tableGroup = createTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블이 아닌 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotEmptyTable() {
        OrderTable emptyTable = tableDao.save(createTable(true));
        OrderTable notEmptyTable = tableDao.save(createTable(false));
        List<OrderTable> tables = Arrays.asList(emptyTable, notEmptyTable);

        TableGroup tableGroup = createTableGroup(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        List<OrderTable> tables1 = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup1 = createTableGroup(tables1);
        tableGroupService.create(tableGroup1);

        OrderTable savedTable3 = tableDao.save(createTable(true));
        List<OrderTable> tables2 = Arrays.asList(savedTable2, savedTable3);

        TableGroup tableGroup2 = createTableGroup(tables2);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTableGroup(tables);
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
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        List<OrderTable> tables = Arrays.asList(savedTable1, savedTable2);

        TableGroup tableGroup = createTableGroup(tables);
        TableGroup create = tableGroupService.create(tableGroup);

        Order order = createOrder(savedTable1);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(create.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
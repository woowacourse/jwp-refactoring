package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
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
    private TableGroup tableGroup;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao tableDao;
    private OrderTable table1;
    private OrderTable table2;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        OrderTable table = OrderTable.builder()
            .empty(true)
            .build();
        table1 = tableDao.save(table);
        table2 = tableDao.save(table);

        tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2))
            .createdDate(LocalDateTime.now())
            .build();
    }

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        TableGroup create = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(create.getId()).isNotNull(),
            () -> assertThat(create.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(create.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_LessTable() {
        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable notExistTable = OrderTable.builder()
            .id(100L)
            .empty(true)
            .build();

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2, notExistTable))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블이 아닌 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotEmptyTable() {
        OrderTable emptyTable = OrderTable.builder()
            .empty(false)
            .build();
        emptyTable = tableDao.save(emptyTable);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2, emptyTable))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2))
            .createdDate(LocalDateTime.now())
            .build();
        tableGroupService.create(tableGroup);

        TableGroup tableGroup2 = TableGroup.builder()
            .orderTables(Arrays.asList(table1, table2))
            .createdDate(LocalDateTime.now())
            .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        TableGroup create = tableGroupService.create(this.tableGroup);

        tableGroupService.ungroup(create.getId());

        OrderTable ungroupedTable1 = tableDao.findById(table1.getId()).get();
        OrderTable ungroupedTable2 = tableDao.findById(table2.getId()).get();
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
        TableGroup create = tableGroupService.create(this.tableGroup);

        Order order = Order.builder()
            .orderTableId(table1.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(create.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
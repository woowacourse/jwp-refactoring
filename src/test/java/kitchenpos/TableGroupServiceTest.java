package kitchenpos;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("TableGroupService 테스트")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao tableDao;

    private OrderTable table1;
    private OrderTable table2;
    private TableGroup tableGroup;

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

    @DisplayName("테이블 그룹 추가 - 성공")
    @Test
    void create() {
        //given
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        //then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("테이블 그룹 추가 - 실패 - Ordertable이 비어있지 않은 경우")
    @Test
    void createFailure() {
        //given
        OrderTable notEmptyTable = OrderTable.builder()
                .empty(false)
                .build();
        OrderTable table2 = OrderTable.builder()
                .empty(true)
                .build();
        tableDao.save(notEmptyTable);
        tableDao.save(table2);
        //when
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(notEmptyTable, table2))
                .createdDate(LocalDateTime.now())
                .build();
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 추가 - 실패 - orderTables의 사이즈가 한 개이하인 경우")
    @Test
    void createFailureWhenInvalidOrderTablesSize() {
        //given
        OrderTable notEmptyTable = OrderTable.builder()
                .empty(true)
                .build();
        tableDao.save(notEmptyTable);
        //when
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(notEmptyTable))
                .createdDate(LocalDateTime.now())
                .build();
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 추가 - 실패 - 저장되지 않은 테이블 그룹 추가")
    @Test
    void createFailureWhenNotExistTable() {
        //given
        OrderTable notExistTable = OrderTable.builder()
                .id(100L)
                .empty(true)
                .build();
        OrderTable table1 = OrderTable.builder()
                .empty(true)
                .build();
        //when
        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(notExistTable, table1))
                .createdDate(LocalDateTime.now())
                .build();
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 추가 - 실패 - 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        OrderTable othersTable = OrderTable.builder()
                .empty(true)
                .build();
        OrderTable table = OrderTable.builder()
                .empty(true)
                .build();
        tableDao.save(othersTable);
        tableDao.save(table);

        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(othersTable, table))
                .createdDate(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 해제 테스트 - 성공")
    @Test
    void ungroup() {
        //given
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        //when
        tableGroupService.ungroup(savedTableGroup.getId());
        OrderTable ungroupedTable1 = tableDao.findById(table1.getId()).get();
        OrderTable ungroupedTable2 = tableDao.findById(table2.getId()).get();
        //then
        assertThat(ungroupedTable1.getTableGroupId()).isNull();
        assertThat(ungroupedTable2.getTableGroupId()).isNull();
        assertThat(ungroupedTable1.isEmpty()).isFalse();
        assertThat(ungroupedTable2.isEmpty()).isFalse();
    }

    @DisplayName("TableGroup 해제 테스트 - 실패 - 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
    @Test
    void ungroup_Fail_With_TableInProgress() {
        Order order = Order.builder()
                .orderTableId(table1.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();
        orderDao.save(order);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

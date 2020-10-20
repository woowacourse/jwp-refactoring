package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static kitchenpos.TestObjectFactory.createOrderTableIdRequest;
import static kitchenpos.TestObjectFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        OrderTable savedTable1 = orderTableDao.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableDao.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest request = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        List<OrderTable> savedTables = orderTableDao
            .findAllByTableGroupId(savedTableGroup.getId());

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
            () -> assertThat(savedTables.get(0).isEmpty()).isFalse(),
            () -> assertThat(savedTables.get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NoTable() {
        OrderTable savedTable1 = orderTableDao.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable savedTable = orderTableDao.save(createOrderTable(true));
        OrderTable notSavedTable = OrderTable.builder()
            .id(1000L)
            .build();
        OrderTableIdRequest savedTableIdRequest = createOrderTableIdRequest(savedTable.getId());
        OrderTableIdRequest notSavedTableIdRequest = createOrderTableIdRequest(
            notSavedTable.getId());
        List<OrderTableIdRequest> tables = Arrays
            .asList(savedTableIdRequest, notSavedTableIdRequest);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable savedTable1 = orderTableDao.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableDao.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable ungroupedTable1 = orderTableDao.findById(savedTable1.getId()).get();
        OrderTable ungroupedTable2 = orderTableDao.findById(savedTable2.getId()).get();
        assertAll(
            () -> assertThat(ungroupedTable1.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable2.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable1.isEmpty()).isFalse(),
            () -> assertThat(ungroupedTable2.isEmpty()).isFalse()
        );
    }

    @Transactional
    @DisplayName("[예외] 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
    @Test
    void ungroup_Fail_With_TableInProgress() {
        OrderTable savedTable1 = orderTableDao.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableDao.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        OrderTable groupedTable = tableGroupDao.findById(savedTableGroup.getId())
            .get()
            .getOrderTables()
            .get(0);
        Order order = createOrder(groupedTable);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
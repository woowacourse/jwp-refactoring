package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderTableIdRequest;
import static kitchenpos.TestObjectFactory.createTable;
import static kitchenpos.TestObjectFactory.createTableGroupRequest;
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
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
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
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest request = createTableGroupRequest(tables);
        TableGroup savedTableGroup = tableGroupService.create(request);

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse(),
            () -> assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NoTable() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable savedTable = tableDao.save(createTable(true));
        OrderTable notSavedTable = OrderTable.builder()
            .id(1000L)
            .build();
        OrderTableIdRequest savedTableIdRequest = createOrderTableIdRequest(savedTable);
        OrderTableIdRequest notSavedTableIdRequest = createOrderTableIdRequest(notSavedTable);
        List<OrderTableIdRequest> tables = Arrays
            .asList(savedTableIdRequest, notSavedTableIdRequest);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블이 아닌 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotEmptyTable() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(false));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_OthersTable() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup1 = createTableGroupRequest(tables);
        tableGroupService.create(tableGroup1);

        OrderTable savedTable3 = tableDao.save(createTable(true));
        OrderTableIdRequest tableIdRequest3 = createOrderTableIdRequest(savedTable3);
        List<OrderTableIdRequest> tables2 = Arrays.asList(tableIdRequest3, tableIdRequest2);

        TableGroupRequest request = createTableGroupRequest(tables2);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable ungroupedTable1 = tableDao.findById(savedTable1.getId()).get();
        OrderTable ungroupedTable2 = tableDao.findById(savedTable2.getId()).get();
        assertAll(
            () -> assertThat(ungroupedTable1.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable2.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable1.isEmpty()).isFalse(),
            () -> assertThat(ungroupedTable2.isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
    @Test
    void ungroup_Fail_With_TableInProgress() {
        OrderTable savedTable1 = tableDao.save(createTable(true));
        OrderTable savedTable2 = tableDao.save(createTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1);
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2);
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = createOrder(savedTable1);
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
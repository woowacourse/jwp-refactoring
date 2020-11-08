package kitchenpos.application;

import static kitchenpos.KitchenposTestHelper.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void createTableGroup() {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, true);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        assertThat(savedTableGroup.getOrderTables()).size().isEqualTo(2);
    }

    @DisplayName("테이블 그룹 생성 요청 수가 2보다 작으면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void createTableGroupSizeException(int size) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            orderTables.add(createOrderTable(Long.valueOf(i + 1), null, 0, true));
        }

        TableGroup tableGroupRequest = createTableGroupRequest(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 테이블을 그룹 지정시 예외 발생")
    @Test
    void createTableGroupByNotExistingOrderTableException() {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, true);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);

        OrderTable notSavedTable = new OrderTable();
        notSavedTable.setId(3L);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2, notSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 지정하려는 테이블이 비어있지 않으면 예외 발생")
    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void createTableGroupByNotProperOrderTableStatusException(Long value) {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, value.equals(1L) ? true : false);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, value.equals(2L) ? true : false);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 지정하려는 테이블이 이미 그룹 지정되어 있으면 예외 발생")
    @Test
    void createTableGroupByOrderTableAlreadyInGroupException() {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest3 = createOrderTable(null, null, 0, true);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);
        OrderTable savedTable3 = orderTableDao.save(tableRequest3);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2));

        tableGroupService.create(tableGroupRequest);

        TableGroup tableGroupRequest2 = createTableGroupRequest(Arrays.asList(savedTable2, savedTable3));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroupTable() {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, true);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        tableGroupService.ungroup(savedTableGroup.getId());

        for (OrderTable orderTable : orderTableDao.findAll()) {
            assertThat(orderTable.getTableGroupId()).isNull();
        }
    }

    @DisplayName("테이블 그룹 해제시 테이블의 주문 상태가 완료가 아닐 경우 예외")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupTableByNotCompleteOrderStatusException(String orderStatus) {
        OrderTable tableRequest1 = createOrderTable(null, null, 0, true);
        OrderTable tableRequest2 = createOrderTable(null, null, 0, true);

        OrderTable savedTable1 = orderTableDao.save(tableRequest1);
        OrderTable savedTable2 = orderTableDao.save(tableRequest2);

        TableGroup tableGroupRequest = createTableGroupRequest(Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        Order order = createOrder(null, savedTable1.getId(), orderStatus, LocalDateTime.now(), Collections.emptyList());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

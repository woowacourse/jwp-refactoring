package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ServiceTest
class TableGroupServiceTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 설정한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(Arrays.asList(savedTable1, savedTable2));

        TableGroup actual = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(actual).extracting(TableGroup::getId).isNotNull(),
            () -> assertThat(actual).extracting(TableGroup::getCreatedDate).isNotNull(),
            () -> assertThat(actual).extracting(TableGroup::getOrderTables,
                InstanceOfAssertFactories.list(OrderTable.class))
                .extracting(OrderTable::getTableGroupId)
                .containsOnly(actual.getId()),
            () -> assertThat(actual).extracting(TableGroup::getOrderTables,
                InstanceOfAssertFactories.list(OrderTable.class))
                .extracting(OrderTable::isEmpty)
                .containsOnly(false)
        );
    }

    @DisplayName("테이블 그룹 생성 시 테이블이 2개 미만일 경우 예외 처리한다.")
    @Test
    void createWithEmptyOrOneTable() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();

        orderTable1.setId(1L);

        tableGroup.setOrderTables(Collections.singletonList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 시 TableId가 존재하지 않을 경우 예외 처리한다.")
    @Test
    void createWithNotExistingTableId() {
        TableGroup tableGroup = new TableGroup();

        OrderTable table1 = new OrderTable();
        OrderTable table2 = new OrderTable();

        table1.setId(1L);
        table2.setId(2L);

        tableGroup.setOrderTables(Arrays.asList(table1, table2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 TableGroup이 이미 지정되어 있으면 예외 처리한다.")
    @Test
    void createWithExistingTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(savedTableGroup.getId());

        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(savedTableGroup.getId());

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(Arrays.asList(savedTable1, savedTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 TableGroup이 비어있지 않다면 예외 처리한다.")
    @Test
    void createWithNotEmpty() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setEmpty(true);

        orderTable2.setEmpty(false);

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(Arrays.asList(savedTable1, savedTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);
        List<OrderTable> savedTables = Arrays.asList(savedTable1, savedTable2);

        tableGroup.setOrderTables(savedTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        assertThat(
            orderTableDao.findAllByIdIn(savedTables.stream().map(OrderTable::getId).collect(Collectors.toList())))
            .extracting(OrderTable::getTableGroupId).containsOnlyNulls();
    }

    @DisplayName("테이블 그룹 해제 시 완료되지 않은 주문이 있을 경우 예외 처리한다.")
    @Test
    void ungroupWithNotCompletedOrder() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);
        List<OrderTable> savedTables = Arrays.asList(savedTable1, savedTable2);

        tableGroup.setOrderTables(savedTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = new Order();
        order.setOrderTableId(savedTable1.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
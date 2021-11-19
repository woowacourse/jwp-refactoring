package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("단체 지정을 생성한다")
    @Test
    void create() {
        // given
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(true), createOrderTable(true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("테이블의 수는 2 이상이어야 한다")
    @Test
    void create_fail_orderTablesSizeShouldGreaterThanOrEqualTo2() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(createOrderTable(true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when,then
        assertThatCode(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("요청한 주문 테이블들이 저장되어있어야 한다")
    @Test
    void create_fail_requestedOrderTablesShouldExists() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(-1L);
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(true), createOrderTable(true), orderTable);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when,then
        assertThatCode(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 모두 비어있어야 한다")
    @Test
    void create_fail_allTablesShouldBeEmpty() {
        // given
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(false), createOrderTable(false));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when,then
        assertThatCode(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 테이블그룹에 연결되어있지 않아야 한다")
    @Test
    void create_fail_allTablesShouldHaveTableGroupId() {
        // given
        OrderTable orderTable1 = createOrderTable(true);
        OrderTable orderTable2 = createOrderTable(true);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        tableGroupService.create(tableGroup);

        TableGroup requestTableGroup = new TableGroup();
        requestTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when,then
        assertThatCode(() -> tableGroupService.create(requestTableGroup))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("단체 지정을 해체한다")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = Arrays.asList(createOrderTable(true), createOrderTable(true));
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId()))
                .allMatch(orderTable -> orderTable.getTableGroupId() == null)
                .allMatch(orderTable -> !orderTable.isEmpty());
    }

    @DisplayName("연결된 주문들의 주문상태는 `계산 완료`여야 한다")
    @Test
    void ungroup_fail_relatedOrdersOrderStatusShouldBeCompletion() {
        // 주문,테이블 생성
        OrderTable savedOrderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);
        Order savedOrder = orderService.create(order);

        // 주문 상태 바꾸기 - 테이블 비우기 시 필요
        Order completionOrder = new Order();
        completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), completionOrder);

        // 테이블 비우기 - tableGroup 생성 시, 주문상태 바꾸기 시 필요
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        tableService.changeEmpty(savedOrderTable.getId(), orderTable);

        // 주문 상태 바꾸기
        Order cookingOrder = new Order();
        cookingOrder.setId(savedOrder.getId());
        cookingOrder.setOrderStatus(OrderStatus.COOKING.name());
        orderDao.save(cookingOrder);

        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = Arrays.asList(savedOrderTable, createOrderTable(true));
        tableGroup.setOrderTables(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when,then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(RuntimeException.class);
    }

    private OrderTable createOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return tableService.create(orderTable);
    }
}
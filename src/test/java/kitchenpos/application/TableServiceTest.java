package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TableServiceTest extends IntegrationTest {

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void create() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0);
        assertThat(savedOrderTable.isEmpty()).isFalse();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void list() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("주문 테이블을 비우거나 채운다")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isEqualTo(empty);
    }

    @DisplayName("주문 테이블이 저장되어있어야 한다")
    @Test
    void changeEmpty_fail_orderTableShouldExists() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(-1L, orderTable))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블이 테이블그룹에 연결되어있지 않아야 한다")
    @Test
    void changeEmpty_fail_orderTableShouldNotRelatedAnyTableGroup() {
        OrderTable savedOrderTable = createOrderTable(true);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable, createOrderTable(true)));
        tableGroupService.create(tableGroup);

        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문들의 주문상태는 `계산 완료`여야 한다")
    @Test
    void changeEmpty_fail_relatedOrdersOrderStatusShouldBeCompletion() {
        OrderTable savedOrderTable = createOrderTable(false);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);
        Order savedOrder = orderService.create(order);

        Order statusOrder = new Order();
        statusOrder.setOrderStatus(OrderStatus.COOKING.name());
        orderService.changeOrderStatus(savedOrder.getId(), statusOrder);

        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);

        // when, then
        assertThatCode(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 테이블의 인원을 수정한다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(false);
        newOrderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = tableService.create(newOrderTable);

        // when
        OrderTable requestOrderTable = new OrderTable();
        requestOrderTable.setNumberOfGuests(2);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), requestOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("주문 테이블은 비어있을 수 없다")
    @Test
    void changeNumberOfGuests_fail_tableCannotBeEmpty() {
        // given
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);
        newOrderTable.setNumberOfGuests(1);
        OrderTable savedOrderTable = tableService.create(newOrderTable);

        // when, then
        OrderTable requestOrderTable = new OrderTable();
        requestOrderTable.setNumberOfGuests(2);
        assertThatCode(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), requestOrderTable))
                .isInstanceOf(RuntimeException.class);
    }

    private OrderTable createOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(0);
        return tableService.create(orderTable);
    }
}
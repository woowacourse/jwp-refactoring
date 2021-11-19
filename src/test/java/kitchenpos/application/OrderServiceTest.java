package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class OrderServiceTest extends IntegrationTest {

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // given
        OrderTable savedOrderTable = createOrderTable();
        OrderLineItem orderLineItem = orderLineItem();
        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("연결된 주문 항목은 비어있으면 안된다")
    @Test
    void create_fail_orderLineItemsCannotBeEmpty() {
        // given
        OrderTable savedOrderTable = createOrderTable();
        Order order = OrderFixture.builder()
                .orderLineItems(Collections.emptyList())
                .orderTableId(savedOrderTable.getId())
                .build();

        // when,then
        assertThatCode(() -> orderService.create(order))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 항목의 메뉴들은 저장되어있어야 한다")
    @Test
    void create_fail_orderLineItemsMenusShouldExists() {
        // given
        OrderTable savedOrderTable = createOrderTable();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(-1L);
        orderLineItem.setQuantity(1);
        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);

        // when,then
        assertThatCode(() -> orderService.create(order))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 테이블이 저장되어있어야 한다")
    @Test
    void create_fail_orderTableShouldExists() {
        // given
        OrderLineItem orderLineItem = orderLineItem();
        Order order = OrderFixture.orderForCreate(-1L, orderLineItem);

        // when,then
        assertThatCode(() -> orderService.create(order))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("연결된 주문 테이블은 비어있을 수 없다")
    @Test
    void create_fail_orderTableCannotBeEmpty() {
        // given
        OrderTable savedOrderTable = createEmptyOrderTable();
        OrderLineItem orderLineItem = orderLineItem();
        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);

        // when,then
        assertThatCode(() -> orderService.create(order))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        // given
        createOrder();

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문의 주문상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        Order createdOrder = createOrder();
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(), order);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("이미 `계산 완료` 상태라면 변경할 수 없다")
    @Test
    void changeOrderStatus_fail() {
        // given
        Order createdOrder = createOrder();
        Order completionOrder = new Order();
        completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(createdOrder.getId(), completionOrder);

        // when
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        assertThatCode(() -> orderService.changeOrderStatus(createdOrder.getId(), order))
                .isInstanceOf(RuntimeException.class);
    }

    private Order createOrder() {
        OrderTable savedOrderTable = createOrderTable();
        OrderLineItem orderLineItem = orderLineItem();
        Order order = OrderFixture.orderForCreate(savedOrderTable.getId(), orderLineItem);
        return orderService.create(order);
    }

    private OrderLineItem orderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }

    private OrderTable createOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        return tableService.create(orderTable);
    }

    private OrderTable createEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(4);
        return tableService.create(orderTable);
    }
}
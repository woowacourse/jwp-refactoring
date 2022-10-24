package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

class OrderServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create()")
    class create {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 주문을 생성한다.")
        void create() {
            // given
            Menu menu = createAndSaveMenu();
            OrderTable savedOrderTable = createAndSaveOrderTable();
            OrderLineItem orderLineItem = createOrderLineItem(menu.getId());

            Order order = createOrder(
                savedOrderTable.getId(),
                null,
                new ArrayList<OrderLineItem>() {{
                    add(orderLineItem);
                }}
            );

            // when
            Order savedOrder = orderService.create(order);

            // then
            assertThat(savedOrder.getId()).isNotNull();
        }

        @Test
        @DisplayName("주문 항목이 비어있는 경우 예외가 발생한다.")
        void nullOrderLineItems() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable();
            Order order = createOrder(savedOrderTable.getId(), null, null);

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문 항목 id인 경우 예외가 발생한다.")
        void invalidOrderLineItemId() {
            // given
            OrderTable savedOrderTable = createAndSaveOrderTable();
            OrderLineItem orderLineItem = createOrderLineItem(0L);

            Order order = createOrder(
                savedOrderTable.getId(),
                null,
                new ArrayList<OrderLineItem>() {{
                    add(orderLineItem);
                }}
            );

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 주문 테이블 id인 경우 예외가 발생한다.")
        void invalidOrderTableId() {
            // given
            Menu menu = createAndSaveMenu();
            OrderLineItem orderLineItem = createOrderLineItem(menu.getId());

            Order order = createOrder(
                0L,
                null,
                new ArrayList<OrderLineItem>() {{
                    add(orderLineItem);
                }}
            );

            // when, then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    @DisplayName("list()")
    class list {

        @Test
        @DisplayName("전체 주문을 조회한다.")
        void list() {
            List<Order> orders = orderService.list();
            assertThat(orders).isNotNull();
        }

    }

    @Nested
    @DisplayName("changeOrderStatus()")
    class changeOrderStatus {

        @Test
        @DisplayName("특정 주문의 주문 상태를 변경한다.")
        void changeOrderStatus() {
            // given
            OrderTable orderTable = createAndSaveOrderTable();
            Order savedOrder = createAndSaveOrder(orderTable.getId(), "COOKING");

            Order order = new Order();
            order.setOrderStatus("MEAL");

            // when
            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
        }

        @Test
        @DisplayName("존재하지 않는 order id인 경우 예외가 발생한다.")
        void wrongInvalidOrderId() {
            // given
            Order order = new Order();
            order.setOrderStatus("MEAL");

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(0L, order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("COMPLETION 상태인 order를 변경하려는 경우 예외가 발생한다.")
        void completionStatus() {
            // given
            OrderTable orderTable = createAndSaveOrderTable();
            Order savedOrder = createAndSaveOrder(orderTable.getId(), "COMPLETION");

            Order order = new Order();
            order.setOrderStatus("COOKING");

            // when, then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
        }

    }

    private Menu createAndSaveMenu() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("menu");
        menu.setPrice(new BigDecimal(1000));
        menu.setMenuGroupId(savedMenuGroup.getId());

        return menuDao.save(menu);
    }

    private OrderTable createAndSaveOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);
        orderTable.setEmpty(false);

        return orderTableDao.save(orderTable);
    }

    private OrderLineItem createOrderLineItem(long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);

        return orderLineItem;
    }

    private Order createOrder(long orderTableId, String status, List<OrderLineItem> items) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(status);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(items);

        return order;
    }

    private Order createAndSaveOrder(long orderTableId, String status) {
        Order order = createOrder(orderTableId, status, null);
        return orderDao.save(order);
    }

}

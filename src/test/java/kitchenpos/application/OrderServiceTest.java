package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("list: 전체 주문 목록을 조회한다.")
    @Test
    void list() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderedTime(LocalDateTime.of(2020, 10, 10, 20, 30));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        final Order savedOrder = orderDao.save(order);
        final Long savedOrderId = savedOrder.getId();
        orderLineItem.setOrderId(savedOrderId);
        orderLineItemDao.save(orderLineItem);

        final List<Order> list = orderService.list();

        assertThat(list).hasSize(1);
    }

    @DisplayName("create: 점유중인 테이블에서 메뉴 중복이 없는 하나 이상의 상품 주문시, 주문 추가 후, 생성된 주문 객체를 반환한다.")
    @Test
    void create() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final Order savedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedTable.getId()),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("create: 점유중인 테이블에서 주문 상품이 없는 경우, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_contains_no_order_line_item() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 점유중이 아닌 테이블에서 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_table_is_empty() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 점유중인 테이블에서 중복된 중복 메뉴를 포함한 상품 들 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_line_item_contains_duplicate_menu() {
        final OrderLineItem firstOrderLineItem = new OrderLineItem();
        firstOrderLineItem.setMenuId(1L);
        firstOrderLineItem.setQuantity(1);

        final OrderLineItem secondOrderLineItem = new OrderLineItem();
        secondOrderLineItem.setMenuId(1L);
        secondOrderLineItem.setQuantity(2);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Lists.list(firstOrderLineItem, secondOrderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeOrderStatus: 완료 되지 않는 주문의 경우, 주문 상태의 변경 요청시, 상태 변경 후, 변경된 주문 객체를 반환한다.")
    @Test
    void changeOrderStatus() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.of(2020, 10, 10, 20, 40));

        final Order savedOrder = orderDao.save(order);
        final Long savedOrderId = savedOrder.getId();
        orderLineItem.setOrderId(savedOrderId);
        orderLineItemDao.save(orderLineItem);

        final Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        final Order updatedOrder = orderService.changeOrderStatus(savedOrderId, newOrder);
        assertAll(
                () -> assertThat(updatedOrder.getId()).isEqualTo(savedOrderId),
                () -> assertThat(updatedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo("MEAL"),
                () -> assertThat(updatedOrder.getOrderTableId()).isEqualTo(savedTable.getId()),
                () -> assertThat(updatedOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("changeOrderStatus: 이미 완료한 주문의 상태의 변경 요청시, 상태 변경 실패 후, IllegalArgumentException 발생.")
    @Test
    void changeOrderStatus_fail_if_order_status_is_completion() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.of(2020, 10, 10, 20, 40));

        final Order savedOrder = orderDao.save(order);
        final Long savedOrderId = savedOrder.getId();
        orderLineItem.setOrderId(savedOrderId);
        orderLineItemDao.save(orderLineItem);

        final Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
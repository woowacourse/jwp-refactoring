package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
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
        orderDao.save(order);

        final List<Order> list = orderService.list();

        assertThat(list).hasSize(1);
    }

    @DisplayName("create: 주문을 생성한다.")
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
        order.setOrderedTime(LocalDateTime.of(2020, 10, 10, 20, 30));
        order.setOrderStatus(OrderStatus.COOKING.name());
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
}
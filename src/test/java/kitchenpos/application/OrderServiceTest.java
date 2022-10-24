package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() {
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);

        assertThat(orderDao.findById(savedOrder.getId())).isPresent();
    }

    @Test
    void create_orderLineItemsEmptyException() {
        Order order = new Order();
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_orderLineItemsSizeException() {
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_orderTableException() {
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTableDao.save(orderTable);
        order.setOrderTableId(0L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        int beforeSize = orderService.list().size();
        orderService.create(order);

        assertThat(orderService.list().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void changeOrderStatus() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(1L, order);

        assertThat(orderDao.findById(savedOrder.getId()).get().getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    void changeOrderStatus_noSuchOrderException() {
        Order order = new Order();

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus_orderStatusException() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(savedOrderTable.getId());

        Order savedOrder = orderService.create(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

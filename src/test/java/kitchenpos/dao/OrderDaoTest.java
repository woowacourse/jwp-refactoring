package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.OrderDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderDaoTest extends DaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void save() throws Exception {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Order foundOrder = orderDao.findById(savedOrder.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrder.getId()).isEqualTo(foundOrder.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(foundOrder.getOrderStatus());
        assertThat(savedOrder.getOrderedTime()).isEqualTo(foundOrder.getOrderedTime());
    }

    @Test
    void findById() throws Exception {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        Order foundOrder = orderDao.findById(savedOrder.getId())
            .orElseThrow(() -> new Exception());
        assertThat(savedOrder.getId()).isEqualTo(foundOrder.getId());
        assertThat(savedOrder.getOrderStatus()).isEqualTo(foundOrder.getOrderStatus());
        assertThat(savedOrder.getOrderedTime()).isEqualTo(foundOrder.getOrderedTime());
    }

    @Test
    void findAll() {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        List<Order> orders = orderDao.findAll();
        assertThat(orders).hasSize(2);
    }

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now()));
        boolean result = orderDao
            .existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.COMPLETION.name()));
        boolean result2 = orderDao
            .existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name()));
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        OrderTable orderTable = orderTableDao.save(new OrderTable());
        orderDao.save(new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now()));
        orderDao.save(new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now()));
        boolean result = orderDao
            .existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.COMPLETION.name()));
        assertThat(result).isTrue();
    }
}

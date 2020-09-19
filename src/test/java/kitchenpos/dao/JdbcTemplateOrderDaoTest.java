package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@SpringBootTest
class JdbcTemplateOrderDaoTest {
    @Autowired
    private JdbcTemplateOrderDao orderDao;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();

        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(1L);
    }

    @Test
    @DisplayName("생성하는 경우")
    void create() {
        Order savedOrder = orderDao.save(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("업데이트하는 경우")
    void update() {
        Order savedOrder = orderDao.save(order);
        Long id = savedOrder.getId();
        String orderStatus = savedOrder.getOrderStatus();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = orderDao.save(savedOrder);

        assertThat(updatedOrder.getId()).isEqualTo(id);
        assertThat(updatedOrder.getOrderStatus()).isNotEqualTo(orderStatus);
    }

    @Test
    void findById() {
        Order savedOrder = orderDao.save(order);
        Order foundOrder = orderDao.findById(savedOrder.getId()).get();

        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus());
        assertThat(foundOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId());
        assertThat(foundOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime());
    }

    @Test
    void findAll() {
        int count = orderDao.findAll().size();
        orderDao.save(order);

        assertThat(orderDao.findAll().size()).isEqualTo(count + 1);
    }

    @Test
    @DisplayName("해당 id의 주문 상태가 해당 주문 상태 리스트에 포함되어 있으면 참")
    void existsByOrderTableIdAndOrderStatusIn() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        Order savedOrder = orderDao.save(order);

        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
            savedOrder.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).isFalse();
    }

    @Test
    @DisplayName("id 리스트 중 하나라도 해당 주문 상태에 포함되어 있으면 참")
    void existsByOrderTableIdInAndOrderStatusIn() {
        Order savedOrder1 = orderDao.save(order);
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        newOrder.setOrderedTime(LocalDateTime.now());
        newOrder.setOrderTableId(1L);
        Order savedOrder2 = orderDao.save(newOrder);

        assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(savedOrder1.getOrderTableId(), savedOrder2.getOrderTableId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).isTrue();
    }
}

package kitchenpos.dao;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@JdbcTest
class JdbcTemplateOrderDaoTest {
    private JdbcTemplateOrderDao orderDao;
    private JdbcTemplateOrderTableDao orderTableDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = DataSourceBuilder.initializeDataSource();
        orderDao = new JdbcTemplateOrderDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);

        OrderTable orderTable = createOrderTable(true);
        orderTableDao.save(orderTable); //1L
    }

    @AfterEach
    void cleanUp() {
        dataSource = DataSourceBuilder.deleteDataSource();
    }

    @Test
    @DisplayName("생성하는 경우")
    void create() {
        Order order = createOrder(OrderStatus.COOKING, 1L);
        Order savedOrder = orderDao.save(order);

        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("업데이트하는 경우")
    void update() {
        Order savedOrder = orderDao.save(createOrder(OrderStatus.COOKING, 1L));

        Long id = savedOrder.getId();
        String orderStatusBeforeUpdate = savedOrder.getOrderStatus();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        Order updatedOrder = orderDao.save(savedOrder);

        assertThat(updatedOrder.getId()).isEqualTo(id);
        assertThat(updatedOrder.getOrderStatus()).isNotEqualTo(orderStatusBeforeUpdate);
    }

    @Test
    void findById() {
        Order savedOrder = orderDao.save(createOrder(OrderStatus.COOKING, 1L));
        Order expectedOrder = orderDao.findById(savedOrder.getId()).get();

        assertAll(
            () -> assertThat(expectedOrder.getId()).isEqualTo(savedOrder.getId()),
            () -> assertThat(expectedOrder.getOrderStatus()).isEqualTo(savedOrder.getOrderStatus()),
            () -> assertThat(expectedOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
            () -> assertThat(expectedOrder.getOrderedTime()).isEqualTo(savedOrder.getOrderedTime())
        );
    }

    @Test
    void findAll() {
        orderDao.save(createOrder(OrderStatus.COOKING, 1L));
        orderDao.save(createOrder(OrderStatus.COOKING, 1L));

        assertThat(orderDao.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("해당 id의 주문 상태가 해당 주문 상태 리스트에 포함되어 있으면 참")
    void existsByOrderTableIdAndOrderStatusIn() {
        Order order = createOrder(OrderStatus.COOKING, 1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        Order savedOrder = orderDao.save(order);

        assertThat(
            orderDao.existsByOrderTableIdAndOrderStatusIn(
                savedOrder.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).isFalse();
    }

    @Test
    @DisplayName("id 리스트 중 하나라도 해당 주문 상태에 포함되어 있으면 참")
    void existsByOrderTableIdInAndOrderStatusIn() {
        Order order = createOrder(OrderStatus.COOKING, 1L);
        Order newOrder = createOrder(OrderStatus.COMPLETION, 1L);
        Order savedOrder1 = orderDao.save(order);
        Order savedOrder2 = orderDao.save(newOrder);

        assertThat(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(savedOrder1.getOrderTableId(), savedOrder2.getOrderTableId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).isTrue();
        assertThat(
            orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(savedOrder1.getOrderTableId(), savedOrder2.getOrderTableId()),
                Arrays.asList(OrderStatus.MEAL.name()))
        ).isFalse();
    }
}

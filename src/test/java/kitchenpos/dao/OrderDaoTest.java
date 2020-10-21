package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import javax.sql.DataSource;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@JdbcTest
@Transactional()
class OrderDaoTest {

    @Autowired
    DataSource dataSource;

    OrderDao orderDao;

    Order order;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
        order = new Order();
        order.setId(null);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(Lists.emptyList());
    }

    @Test
    void name() {
    }

    @Test
    void save() {
        Order savedOrder = orderDao.save(order);
        assertThat(savedOrder).isEqualToIgnoringGivenFields(order, "id");
        assertThat(savedOrder).extracting(Order::getId).isEqualTo(1L);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @DisplayName("Order Table Id가 일치하고, status가 포함되는 지 확인한다.")
    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        orderDao.save(order);
        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
            order.getOrderTableId(), Collections.singletonList(OrderStatus.COMPLETION.name()))).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
    }
}
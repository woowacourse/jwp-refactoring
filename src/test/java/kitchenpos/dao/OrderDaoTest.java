package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class OrderDaoTest {

    @Autowired
    DataSource dataSource;

    OrderDao orderDao;

    Order order1;
    Order order2;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);

        order1 = OrderFixture.createWithoutId(OrderStatus.COMPLETION.name(), 1L, null);
        order2 = OrderFixture.createWithoutId(OrderStatus.COOKING.name(), 1L, null);
    }

    @DisplayName("Save Order")
    @Test
    void save() {
        Order savedOrder = orderDao.save(order1);

        assertThat(savedOrder).isEqualToIgnoringGivenFields(order1, "id");
        assertThat(savedOrder).extracting(Order::getId).isEqualTo(1L);
    }

    @DisplayName("Select Order")
    @Test
    void findById() {
        Order savedOrder = orderDao.save(order1);

        assertThat(orderDao.findById(savedOrder.getId()).get())
            .isEqualToComparingFieldByField(savedOrder);
    }

    @DisplayName("Select All")
    @Test
    void findAll() {
        Order savedOrder1 = orderDao.save(order1);
        Order savedOrder2 = orderDao.save(order2);

        assertThat(orderDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(savedOrder1, savedOrder2));
    }

    @DisplayName("Order Table Id가 일치하고, status가 포함되는 Order가 존재하는 지 확인한다.")
    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        orderDao.save(order1);
        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
            order1.getOrderTableId(), Collections.singletonList(OrderStatus.COMPLETION.name()))).isTrue();
    }

    @DisplayName("Order Table Id와 Status를 기준으로 포함되는 Order가 존재하는 지 확인한다.")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        orderDao.save(order1);
        orderDao.save(order2);

        assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COMPLETION.name(), OrderStatus.COOKING.name()))).isTrue();
    }
}

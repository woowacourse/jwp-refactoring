package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderDaoTest {
    @Autowired
    private DataSource dataSource;
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    public void save() {
        //given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus("COOKING");
        order.setOrderedTime(LocalDateTime.now());

        //when
        Order returnedOrder = orderDao.save(order);

        //then
        assertThat(returnedOrder.getId()).isNotNull();
        assertThat(order.getOrderTableId()).isEqualTo(returnedOrder.getOrderTableId());
        assertThat(order.getOrderStatus()).isEqualTo(returnedOrder.getOrderStatus());
        assertThat(order.getOrderedTime()).isEqualTo(returnedOrder.getOrderedTime());
    }

    @Test
    @DisplayName("주문을 조회한다.")
    public void findById() {
        //given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus("MEAL");
        order.setOrderedTime(LocalDateTime.now());
        final Order savedOrder = orderDao.save(order);

        //when
        Order returnedOrder = orderDao.findById(savedOrder.getId()).get();

        //then
        assertThat(order.getOrderTableId()).isEqualTo(returnedOrder.getOrderTableId());
        assertThat(order.getOrderStatus()).isEqualTo(returnedOrder.getOrderStatus());
        assertThat(order.getOrderedTime()).isEqualTo(returnedOrder.getOrderedTime());
    }

    @Test
    @DisplayName("모든 주문을 조회한다.")
    public void findAll() {
        //given
        final int originalSize = orderDao.findAll().size();
        Order order1 = new Order();
        order1.setOrderTableId(1L);
        order1.setOrderStatus("COOKING");
        order1.setOrderedTime(LocalDateTime.now());
        final Order savedOrder1 = orderDao.save(order1);

        Order order2 = new Order();
        order2.setOrderTableId(2L);
        order2.setOrderStatus("MEAL");
        order2.setOrderedTime(LocalDateTime.now());
        final Order savedOrder2 = orderDao.save(order2);

        //when
        List<Order> returnedOrders = orderDao.findAll();

        //then
        assertThat(returnedOrders).hasSize(2 + originalSize);
        assertThat(returnedOrders).contains(savedOrder1);
        assertThat(returnedOrders).contains(savedOrder2);
    }

    @Test
    @DisplayName("특정 주문테이블과 주문상태로 주문이 존재하는지 확인한다.")
    public void existsByOrderTableIdAndOrderStatusIn() {
        //given
        Order order1 = new Order();
        order1.setOrderTableId(1L);
        order1.setOrderStatus("COOKING");
        order1.setOrderedTime(LocalDateTime.now());
        final Order savedOrder1 = orderDao.save(order1);

        Order order2 = new Order();
        order2.setOrderTableId(2L);
        order2.setOrderStatus("MEAL");
        order2.setOrderedTime(LocalDateTime.now());
        orderDao.save(order2);

        //when
        boolean exists = orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrder1.getId(), List.of("COOKING"));

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 주문테이블과 주문상태로 주문이 존재하는지 확인한다.")
    public void existsByOrderTableIdInAndOrderStatusIn() {
        //given
        Order order1 = new Order();
        order1.setOrderTableId(1L);
        order1.setOrderStatus("COOKING");
        order1.setOrderedTime(LocalDateTime.now());
        final Order savedOrder1 = orderDao.save(order1);

        Order order2 = new Order();
        order2.setOrderTableId(2L);
        order2.setOrderStatus("MEAL");
        order2.setOrderedTime(LocalDateTime.now());
        final Order savedOrder2 = orderDao.save(order2);

        //when
        boolean exists = orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(savedOrder1.getId(), savedOrder2.getId()), List.of("COOKING", "MEAL"));

        //then
        assertThat(exists).isTrue();
    }
}

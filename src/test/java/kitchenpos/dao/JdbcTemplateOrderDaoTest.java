package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTemplateOrderDaoTest extends JdbcTemplateTest {

    private OrderDao orderDao;
    private Order order;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
        order = makeOrder();
    }

    @Test
    void 주문을_저장한다() {
        Order saved = orderDao.save(order);

        assertThat(saved.getOrderStatus()).isEqualTo(order.getOrderStatus());
        assertThat(saved.getOrderedTime()).isEqualTo(order.getOrderedTime());
        assertThat(saved.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @Test
    void 식별자로_주문을_조회한다() {
        Order saved = orderDao.save(order);

        Order expected = orderDao.findById(saved.getId()).get();

        assertThat(expected.getId()).isEqualTo(saved.getId());
    }

    @Test
    void 모든_주문을_조회한다() {
        Order order1 = makeOrder();
        Order order2 = makeOrder();

        orderDao.save(order1);
        orderDao.save(order2);

        List<Order> orders = orderDao.findAll();

        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void 주문테이블_식별자와_주문_상태로_주문을_조회한다() {
        orderDao.save(order);

        boolean expected = orderDao.existsByOrderTableIdAndOrderStatusIn(1L, List.of("진행중"));

        assertThat(expected).isTrue();
    }

    @Test
    void 주문테이블_식별자들과_주문_상태로_주문을_조회한다() {
        orderDao.save(order);

        boolean expected = orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L), List.of("진행중"));

        assertThat(expected).isTrue();
    }

    private Order makeOrder() {
        Order order = new Order();
        order.setOrderStatus("진행중");
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(1L);
        return order;
    }
}

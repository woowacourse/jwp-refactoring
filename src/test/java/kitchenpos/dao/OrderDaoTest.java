package kitchenpos.dao;

import static kitchenpos.fixture.OrderFixture.ORDER_FIXTURE_1;
import static kitchenpos.fixture.OrderFixture.ORDER_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    void save() {
        Order order = ORDER_FIXTURE_1;

        Order persistOrder = orderDao.save(order);

        assertThat(persistOrder.getId()).isNotNull();
    }

    @Test
    void findById() {
        Order persistOrder = orderDao.save(ORDER_FIXTURE_1);

        Order findOrder = orderDao.findById(persistOrder.getId()).get();

        assertThat(findOrder).isEqualToComparingFieldByField(persistOrder);
    }

    @Test
    void findAll() {
        orderDao.save(ORDER_FIXTURE_1);
        orderDao.save(ORDER_FIXTURE_2);

        List<Order> orders = orderDao.findAll();
        List<LocalDateTime> orderedDateTime = orders.stream()
            .map(Order::getOrderedTime)
            .collect(Collectors.toList());

        assertThat(orderedDateTime).contains(ORDER_FIXTURE_1.getOrderedTime(), ORDER_FIXTURE_2.getOrderedTime());
    }

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        orderDao.save(ORDER_FIXTURE_1);
        orderDao.save(ORDER_FIXTURE_2);

        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(ORDER_FIXTURE_1.getOrderTableId(),
            Arrays.asList("COOKING"))
        ).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        orderDao.save(ORDER_FIXTURE_1);
        orderDao.save(ORDER_FIXTURE_2);
        assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList("MEAL"))
        ).isTrue();
    }
}

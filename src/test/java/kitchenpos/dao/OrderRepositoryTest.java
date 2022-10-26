package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderRepositoryTest {

    private OrderRepository orderRepository;

    @Autowired
    public OrderRepositoryTest(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    void save() {
        // given
        Order order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
        // when
        Order savedOrder = orderRepository.save(order);
        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Order order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
        Order savedOrder = orderRepository.save(order);

        // when
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        // then
        assertThat(foundOrder).isPresent();
    }

    @Test
    void findAll() {
        // given
        Order order = new Order(1L, "COOKING", LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1);
    }

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        long orderTableId = 1L;
        String orderStatus = "COOKING";

        Order order = new Order(orderTableId, orderStatus, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 2)));
        orderRepository.save(order);

        // when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(orderStatus));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        long orderTableIdA = 1L;
        String orderStatusA = "COOKING";
        long orderTableIdB = 1L;
        String orderStatusB = "MEAL";

        Order orderA = new Order(orderTableIdA, orderStatusA, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 2)));
        Order orderB = new Order(orderTableIdB, orderStatusB, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 2)));
        orderRepository.save(orderA);
        orderRepository.save(orderB);

        // when
        boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTableIdA, orderTableIdB), List.of(orderStatusA)
        );

        // then
        assertThat(exists).isTrue();
    }
}

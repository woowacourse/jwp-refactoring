package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kitchenpos.OrderFixtures;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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
        Order order = OrderFixtures.createOrder();
        // when
        Order savedOrder = orderRepository.save(order);
        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Order order = OrderFixtures.createOrder();
        Order savedOrder = orderRepository.save(order);

        // when
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        // then
        assertThat(foundOrder).isPresent();
    }

    @Test
    void findAll() {
        // given
        Order order = OrderFixtures.createOrder();
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1);
    }

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        Order order = OrderFixtures.createOrder();
        orderRepository.save(order);
        long orderTableId = order.getOrderTable().getId();
        OrderStatus orderStatus = OrderStatus.COOKING;

        // when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(orderStatus));

        // then
        assertThat(exists).isTrue();
    }
}

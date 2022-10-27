package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.OrderFixtures;
import kitchenpos.OrderTableFixtures;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
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
        String orderStatus = "COOKING";

        // when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(orderStatus));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        OrderTable orderTableA = OrderTableFixtures.createOrderTable(1L, null, 2, false);
        OrderTable orderTableB = OrderTableFixtures.createOrderTable(2L, null, 4, false);
        Order orderA = OrderFixtures.createOrder(orderTableA);
        Order orderB = OrderFixtures.createOrder(orderTableB);

        orderRepository.save(orderA);
        orderRepository.save(orderB);

        // when
        boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTableA.getId(), orderTableB.getId()), List.of(orderA.getOrderStatus())
        );

        // then
        assertThat(exists).isTrue();
    }
}

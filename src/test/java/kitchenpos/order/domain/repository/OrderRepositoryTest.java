package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void existsByOrderTableIdAndOrderStatusIn() {
        //given
        orderRepository.save(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()));

        //when
        final boolean result = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, List.of(OrderStatus.COOKING));

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsByOrderTableIdAndOrderStatusInIfNotMatchOrderStatus() {
        //given
        orderRepository.save(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()));

        //when
        final boolean result = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, List.of(OrderStatus.MEAL));

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusIn() {
        //given
        orderRepository.save(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()));

        //when
        final boolean result = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(1L), List.of(OrderStatus.COOKING));

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsByOrderTableIdInAndOrderStatusInIfNotMatchOrderTableId() {
        //given
        orderRepository.save(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()));

        //when
        final boolean result = orderRepository.existsByOrderTableIdInAndOrderStatusIn(List.of(3L), List.of(OrderStatus.COOKING));

        //then
        assertThat(result).isFalse();
    }
}

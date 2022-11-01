package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다.")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_true_case() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now());
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId(), orderTable2.getId()),
                        List.of(OrderStatus.COOKING, OrderStatus.MEAL));

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다(status가 없는 경우)")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_false_case() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now());
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId(), orderTable2.getId()),
                        List.of(OrderStatus.COMPLETION));

        // then
        assertThat(exists).isFalse();
    }

    @DisplayName("TableId와 OrderStatus로 Order를 찾아온다(tableId가 없는 경우)")
    @Test
    void existsByOrderTableIdInAndOrderStatusIn_false_case2() {
        // given
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(4, false));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(5, false));
        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
        final Order order2 = new Order(orderTable2, OrderStatus.MEAL, LocalDateTime.now());
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        final boolean exists = orderRepository
                .existsByOrderTableIdInAndOrderStatusIn(List.of(999L),
                        List.of(OrderStatus.COOKING, OrderStatus.MEAL));

        // then
        assertThat(exists).isFalse();
    }
}

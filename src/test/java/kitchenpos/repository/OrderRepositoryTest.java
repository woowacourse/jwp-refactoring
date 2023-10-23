package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.DataCleaner;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private DataCleaner dataCleaner;

    @BeforeEach
    void setUp() {
        dataCleaner.clear();
    }

    @DisplayName("주어진 주문 상태들, 테이블들 중 하나라도 가지는 주문이 있는지 확인한다.")
    @Test
    void existsByOrderTableInAndOrderStatusIn() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, false);
        final OrderTable orderTable2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(orderTable1, orderTable2));

        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
        final Order order2 = new Order(orderTable2, OrderStatus.COOKING, LocalDateTime.now());
        orderRepository.saveAll(List.of(order1, order2));

        // when
        final boolean exists = orderRepository.existsByOrderTableInAndOrderStatusIn(List.of(orderTable1), List.of(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("주어진 주문 상태들 중 하나라도 가지고, 특정 테이블을 가지는 주문이 있는지 확인한다.")
    @Test
    void existsByOrderTableAndOrderStatusIn() {
        // given
        final OrderTable orderTable1 = new OrderTable(3, false);
        orderTableRepository.save(orderTable1);

        final Order order1 = new Order(orderTable1, OrderStatus.COOKING, LocalDateTime.now());
        orderRepository.save(order1);

        // when
        final boolean exists = orderRepository.existsByOrderTableAndOrderStatusIn(orderTable1, List.of(OrderStatus.COOKING));

        // then
        assertThat(exists).isTrue();
    }
}

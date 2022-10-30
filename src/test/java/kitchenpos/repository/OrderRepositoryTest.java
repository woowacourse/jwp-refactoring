package kitchenpos.repository;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotConvertableStatusException;
import kitchenpos.exception.NotFoundOrderTableException;
import org.junit.jupiter.api.Test;

class OrderRepositoryTest extends RepositoryTest {

    @Test
    void 주문을_저장한다() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Order order = new Order(savedOrderTable.getId(), MEAL, LocalDateTime.now());

        Order savedOrder = orderRepository.save(order, List.of(new OrderLineItem(1L, 1)));

        assertThat(orderDao.findById(savedOrder.getId())).isPresent();
    }

    @Test
    void 주문을_저장할때_orderTableId가_존재하지않으면_예외를_발생한다() {
        Order order = new Order(0L, MEAL, LocalDateTime.now());

        assertThatThrownBy(() -> orderRepository.save(order, List.of()))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 주문_목록을_가져온다() {
        Order order = new Order(1L, MEAL, LocalDateTime.now());

        int beforeSize = orderRepository.findAll().size();
        orderDao.save(order);

        assertThat(orderRepository.findAll().size()).isEqualTo(beforeSize + 1);
    }

    @Test
    void 주문_상태를_변경한다() {
        Order order = new Order(1L, COOKING, LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        orderRepository.changeOrderStatus(savedOrder.getId(), MEAL);

        assertThat(orderDao.findById(savedOrder.getId()).get().getOrderStatus()).isEqualTo(MEAL.name());
    }

    @Test
    void 주문_상태를_변경할때_완료상태면_예외를_반환한다() {
        Order order = new Order(1L, COMPLETION, LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> orderRepository.changeOrderStatus(savedOrder.getId(), MEAL))
                .isInstanceOf(NotConvertableStatusException.class);
    }
}

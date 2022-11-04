package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderDao 는 ")
@SpringTestWithData
class OrderRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문을 저장한다.")
    @Test
    void save() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = orderRepository.save(
                new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItem(null, 1L))));

        assertThat(order.getId()).isGreaterThanOrEqualTo(1L);
    }

    @DisplayName("특정 주문을 찾는다.")
    @Test
    void findById() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order order = orderRepository.save(
                new Order(savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItem(null, 1L))));

        final Order actual = orderRepository.findById(order.getId())
                .get();

        assertThat(actual.getId()).isEqualTo(order.getId());
    }

    @DisplayName("주문 테이블 id로 주문을 찾는다.")
    @Test
    void findByTableId() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order savedOrder = orderRepository.save(new Order(savedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        final Order actual = orderRepository.findByTableId(savedOrder.getOrderTableId())
                .get();

        assertThat(actual.getId()).isEqualTo(savedOrder.getId());
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAll() {
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        final Order savedOrder = orderRepository.save(new Order(savedOrderTable.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L))));

        final List<OrderTable> orderTables = orderTableRepository.findAll();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(1),
                () -> assertThat(orderTables.get(0).getId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 테이블 id로 주문 항목을 찾는다.")
    @Test
    void findAllByTableIds() {
        final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final Order order1 = new Order(orderTable1.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L)));
        final Order order2 = new Order(orderTable2.getId(), OrderStatus.MEAL, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        final List<Order> orders = orderRepository.findAllByOrderTableId(
                List.of(orderTable1.getId(), orderTable2.getId()));

        assertThat(orders.size()).isEqualTo(2);
    }
}

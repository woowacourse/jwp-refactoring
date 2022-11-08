package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("주문을 저장한다.")
    void save() {
        final LocalDateTime startTime = LocalDateTime.now();
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order = Order.create(orderTable1.getId(), List.of(orderLineItem));

        final Order savedOrder = orderRepository.save(order);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(savedOrder.getOrderedTime()).isBefore(LocalDateTime.now()),
                () -> assertThat(savedOrder.getOrderedTime()).isAfter(startTime),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    @DisplayName("주문을 id로 조회한다.")
    void findById() {
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order = Order.create(orderTable1.getId(), List.of(orderLineItem));
        final Order savedOrder = orderRepository.save(order);

        final Order findOrder = orderRepository.findById(savedOrder.getId()).orElseThrow();

        assertAll(
                () -> assertThat(findOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(findOrder.getOrderTableId()).isEqualTo(orderTable1.getId()),
                () -> assertThat(findOrder.getOrderedTime()).isEqualTo(order.getOrderedTime()),
                () -> assertThat(findOrder.getOrderStatus()).isEqualTo(order.getOrderStatus()),
                () -> assertThat(findOrder.getOrderLineItems()).hasSize(savedOrder.getOrderLineItems().size())
        );
    }

    @Test
    @DisplayName("주문정보 전체를 조회한다.")
    void findAll() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order1 = Order.create(orderTable1.getId(), List.of(orderLineItem1));
        final Order savedOrder1 = orderRepository.save(order1);

        final OrderLineItem orderLineItem2 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order2 = Order.create(orderTable2.getId(), List.of(orderLineItem2));
        final Order savedOrder2 = orderRepository.save(order2);

        final List<Order> orders = orderRepository.findAll();

        assertAll(
                () -> assertThat(orders).extracting("id")
                        .contains(savedOrder1.getId(), savedOrder2.getId()),
                () -> assertThat(orders).extracting("orderTableId")
                        .contains(savedOrder1.getOrderTableId(), savedOrder2.getOrderTableId())
        );
    }

    @Test
    @DisplayName("테이블 id와 주문 상태 목록 조건으로 주문이 있는지 확인한다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order1 = Order.create(orderTable1.getId(), List.of(orderLineItem1));
        orderRepository.save(order1);

        final OrderLineItem orderLineItem2 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order2 = Order.create(orderTable2.getId(), List.of(orderLineItem2));
        final Order savedOrder2 = orderRepository.save(order2);
        savedOrder2.changeOrderStatus(OrderStatus.MEAL);
        orderRepository.save(savedOrder2);

        final boolean existTrue = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable2.getId(),
                List.of(OrderStatus.MEAL)
        );

        final boolean existFalse = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable1.getId(),
                List.of(OrderStatus.MEAL)
        );

        assertAll(
                () -> assertThat(existTrue).isTrue(),
                () -> assertThat(existFalse).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 id 목록과 주문상태 목록을 조건으로 주문이 있는지 확인한다.")
    void existsByOrderTableIdInAndOrderStatusIn() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order1 = Order.create(orderTable1.getId(), List.of(orderLineItem1));
        orderRepository.save(order1);

        final OrderLineItem orderLineItem2 = new OrderLineItem(menu.getName(), menu.getPrice(), menu.getId(), 1L);
        final Order order2 = Order.create(orderTable2.getId(), List.of(orderLineItem2));
        final Order savedOrder2 = orderRepository.save(order2);
        savedOrder2.changeOrderStatus(OrderStatus.MEAL);
        orderRepository.save(savedOrder2);

        final boolean existTrue = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTable1.getId(), orderTable2.getId()),
                List.of(OrderStatus.MEAL)
        );

        final boolean existFalse = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                List.of(orderTable1.getId(), orderTable2.getId()),
                List.of(OrderStatus.COMPLETION)
        );

        assertAll(
                () -> assertThat(existTrue).isTrue(),
                () -> assertThat(existFalse).isFalse()
        );
    }
}
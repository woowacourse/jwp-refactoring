package kitchenpos.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderRepositoryTest(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    void 저장한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));

        // when
        Order savedOrder = orderRepository.save(order);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(1L)
        );
    }

    private Order order_객체를_생성한다(final Long orderTableId,
                                 final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    @Test
    void 이미_ID가_존재하면_update를_진행한다(@Autowired EntityManager entityManager) {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
        Order savedOrder = orderRepository.save(order);

        // when
        savedOrder.changeOrderStatus(MEAL);
        entityManager.flush();

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(MEAL)
        );
    }

    @Test
    void ID로_order를_조회한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
        Order savedOrder = orderRepository.save(order);

        // when
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        // then
        Assertions.assertAll(
                () -> assertThat(foundOrder).isPresent(),
                () -> assertThat(foundOrder.get())
                        .usingRecursiveComparison()
                        .ignoringFields("orderedTime")
                        .isEqualTo(
                                new Order(
                                        savedOrder.getId(),
                                        savedOrder.getOrderTableId(),
                                        savedOrder.getOrderStatus(),
                                        savedOrder.getOrderedTime(),
                                        savedOrder.getOrderLineItems()
                                )
                        )
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        Optional<Order> order = orderRepository.findById(101L);
        assertThat(order).isEmpty();
    }

    @Test
    void order_목록을_조회한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
        Order savedOrder = orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("orderedTime")
                .isEqualTo(
                        Arrays.asList(new Order(savedOrder.getId(),
                                order.getOrderTableId(),
                                order.getOrderStatus(),
                                order.getOrderedTime(),
                                order.getOrderLineItems()
                        ))
                );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "COOKING, COMPLETION, true",
            "COOKING, MEAL, true",
    })
    void order_status들과_order_table_id에_맞는_order가_있는지_확인한다(OrderStatus orderStatus1, OrderStatus orderStatus2,
                                                           boolean expected) {
        // given
        Order order1 = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
        Order order2 = order_객체를_생성한다(1L, Arrays.asList(new OrderLineItem(1L, 3L)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(COOKING, MEAL));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, COOKING, 2, COMPLETION, true",
            "1, COOKING, 2, MEAL, true",
            "3, COOKING, 4, MEAL, false",
    })
    void order_status들과_order_table_id들에_맞는_order가_있는지_확인한다(
            long orderTableId1,
            OrderStatus orderStatus1,
            long orderTableId2,
            OrderStatus orderStatus2,
            boolean expected
    ) {
        // given
        Order order1 = order_객체를_생성한다(orderTableId1,
                Arrays.asList(new OrderLineItem(1L, 3L)));
        Order order2 = order_객체를_생성한다(orderTableId2,
                Arrays.asList(new OrderLineItem(1L, 3L)));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L), Arrays.asList(COOKING, MEAL)
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fetch_join으로_order를_조회한다() {
        // given
        Long menuId = 1L;
        Long quantity = 3L;
        OrderLineItem orderLineItem = new OrderLineItem(menuId, quantity);
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAllWithOrderLineItems();

        // then
        Assertions.assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getOrderLineItems()).hasSize(1)
        );
    }
}

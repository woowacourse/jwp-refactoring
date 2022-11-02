package kitchenpos.repository;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    private Long menuId = 1L;
    private OrderLineItem orderLineItem = new OrderLineItem(menuId, "pasta", BigDecimal.valueOf(13000), 3L);

    @Autowired
    public OrderRepositoryTest(final OrderRepository orderRepository, final MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    @Test
    void 저장한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));

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
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
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
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
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
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderedTime", "orderLineItems.menuPrice", "orderLineItems.seq")
                .isEqualTo(
                        Arrays.asList(new Order(1L, Arrays.asList(orderLineItem)))
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
        Order order1 = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
        Order order2 = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(COOKING, MEAL));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 2, true",
            "4, 5, false",
    })
    void order_status들과_order_table_id들에_맞는_order가_있는지_확인한다(
            long orderTableId1,
            long orderTableId2,
            boolean expected
    ) {
        // given
        Order order1 = order_객체를_생성한다(orderTableId1, Arrays.asList(orderLineItem));
        Order order2 = order_객체를_생성한다(orderTableId2, Arrays.asList(orderLineItem));
        orderRepository.save(order1);
        orderRepository.save(order2);

        orderRepository.findAll()
                .forEach(order -> System.out.println(
                        "order.getOrderTableId() = " + order.getOrderTableId() + ", " + order.getOrderStatus()));
        // when
        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(2L, 3L), Arrays.asList(COOKING, MEAL)
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void fetch_join으로_order를_조회한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
        orderRepository.save(order);

        // when
        List<Order> orders = orderRepository.findAllWithOrderLineItems();

        // then
        Assertions.assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(orders.get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    void menu_이름이_변경되어도_기존_주문_항목에는_변화가_없어야한다() {
        // given
        Order order = order_객체를_생성한다(1L, Arrays.asList(orderLineItem));
        Order savedOrder = orderRepository.save(order);
        String beforeMenuName = savedOrder.getOrderLineItems().get(0).getMenuName();

        Menu menu = menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
        menu.changeMenuName("ramen");
        menuRepository.save(menu);

        // when
        Order afterOrder = orderRepository.findById(savedOrder.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(afterOrder.getOrderLineItems().get(0).getMenuName()).isEqualTo(beforeMenuName);
    }
}

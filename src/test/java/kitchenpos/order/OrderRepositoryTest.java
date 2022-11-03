package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.fixtures.MenuFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import kitchenpos.support.fixtures.OrderTableFixtures;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("주문을 저장한다")
    void save() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));

        // when
        final Order saved = orderRepository.save(order);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getOrderTableId()).isEqualTo(savedOrderTable.getId()),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
                () -> assertThat(saved.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블로 주문을 저장하려고 하면 예외가 발생한다")
    void saveExceptionNotExistOrderTable() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, -1L, OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderRepository.save(order))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문을 조회한다")
    void findById() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        final Order saved = orderRepository.save(order);

        // when
        final Order foundOrder = orderRepository.findById(saved.getId())
                .get();

        // then
        assertThat(foundOrder).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("id로 주문을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<Order> order = orderRepository.findById(-1L);

        // then
        assertThat(order).isEmpty();
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void findAll() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        final Order saved = orderRepository.save(order);

        // when
        final List<Order> orders = orderRepository.findAll();

        // then
        assertAll(
                () -> assertThat(orders).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orders).extracting("id")
                        .contains(saved.getId())
        );
    }

    @Test
    @DisplayName("주문 테이블 id와 주문 상태가 모두 일치하는 주문이 존재하는지 확인한다")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        orderRepository.save(order);

        // when
        final boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(),
                Collections.singletonList(OrderStatus.MEAL.name()));

        final boolean notMatchOrderStatus = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                savedOrderTable.getId(),
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        final boolean notExistsOrderTable = orderRepository.existsByOrderTableIdAndOrderStatusIn(-1L,
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        // then
        assertAll(
                () -> assertThat(exists).isTrue(),
                () -> assertThat(notMatchOrderStatus).isFalse(),
                () -> assertThat(notExistsOrderTable).isFalse()
        );
    }

    @Test
    @DisplayName("주어진 주문 테이블 id들 중에서 주문 상태가 일치하는 주문이 존재하는지 확인한다")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        orderRepository.save(order);

        // when
        final boolean exists = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(savedOrderTable.getId()),
                Collections.singletonList(OrderStatus.MEAL.name()));

        final boolean notMatchOrderStatus = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(savedOrderTable.getId()),
                Collections.singletonList(OrderStatus.COMPLETION.name()));

        final boolean notExistsOrderTable = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Collections.singletonList(-1L),
                Collections.singletonList(OrderStatus.MEAL.name()));

        // then
        assertAll(
                () -> assertThat(exists).isTrue(),
                () -> assertThat(notMatchOrderStatus).isFalse(),
                () -> assertThat(notExistsOrderTable).isFalse()
        );
    }
}

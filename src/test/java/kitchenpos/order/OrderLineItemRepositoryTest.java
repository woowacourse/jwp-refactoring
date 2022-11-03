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
import kitchenpos.support.fixtures.OrderFixtures;
import kitchenpos.support.fixtures.OrderLineItemFixtures;
import kitchenpos.support.fixtures.OrderTableFixtures;
import kitchenpos.support.repository.OrderLineItemRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class OrderLineItemRepositoryTest {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("주문 항목을 저장한다")
    void save() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableIdAndOrderLineItems(savedOrderTable.getId(),
                orderLineItem);
        final Order savedOrder = orderRepository.save(order);

        // when
        final OrderLineItem saved = orderLineItemRepository.save(orderLineItem);

        // then
        assertAll(
                () -> assertThat(saved.getSeq()).isNotNull(),
                () -> assertThat(saved.getOrder()).isEqualTo(savedOrder),
                () -> assertThat(saved.getOrderMenu().getId()).isNotNull(),
                () -> assertThat(saved.getQuantity()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("존재하지 않은 주문으로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistOrder() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        orderTableRepository.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemRepository.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않은 메뉴로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, menu, 2);
        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableIdAndOrderLineItems(savedOrderTable.getId(),
                orderLineItem);
        final Order savedOrder = orderRepository.save(order);
        final OrderLineItem invalidOrderLineItem = OrderLineItemFixtures.toOrderLineItem(savedOrder, null, 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemRepository.save(invalidOrderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableIdAndOrderLineItems(savedOrderTable.getId(),
                orderLineItem);
        orderRepository.save(order);

        // when
        final OrderLineItem foundOrderLineItem = orderLineItemRepository.findById(orderLineItem.getSeq())
                .get();

        // then
        assertThat(foundOrderLineItem).usingRecursiveComparison()
                .isEqualTo(orderLineItem);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회할 때 결과가 없다면 Optional.empty를 반환한다")
    void findByIdNotExist() {
        // when
        final Optional<OrderLineItem> orderLineItem = orderLineItemRepository.findById(-1L);

        // then
        assertThat(orderLineItem).isEmpty();
    }

    @Test
    @DisplayName("모든 주문 항목을 조회한다")
    void findByAll() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableIdAndOrderLineItems(savedOrderTable.getId(),
                orderLineItem);
        orderRepository.save(order);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .contains(orderLineItem.getSeq())
        );
    }

    @Test
    @DisplayName("주문 id로 모든 주문 항목을 조회한다")
    void findByAllByOrderId() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, savedMenu, 2);
        final Order order = new Order(null, savedOrderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                Collections.singletonList(orderLineItem));
        final Order savedOrder = orderRepository.save(order);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSize(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .isNotEmpty()
        );
    }
}

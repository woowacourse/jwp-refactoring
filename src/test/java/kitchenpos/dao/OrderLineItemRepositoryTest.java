package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.MenuFixtures;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.OrderTableFixtures;
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

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder, savedMenu.getId(), 2);

        // when
        final OrderLineItem saved = orderLineItemRepository.save(orderLineItem);

        // then
        assertAll(
                () -> assertThat(saved.getSeq()).isNotNull(),
                () -> assertThat(saved.getOrder()).isEqualTo(savedOrder),
                () -> assertThat(saved.getMenuId()).isEqualTo(savedMenu.getId()),
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

        final Order notSavedOrder = OrderFixtures.COOKING_ORDER.create();
        final OrderLineItem orderLineItem = new OrderLineItem(null, notSavedOrder, savedMenu.getId(), 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemRepository.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않은 메뉴로 주문 항목을 저장하면 예외가 발생한다")
    void saveExceptionNotExistMenu() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder, -1L, 2);

        // when, then
        assertThatThrownBy(() -> orderLineItemRepository.save(orderLineItem))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("id로 주문 항목을 조회한다")
    void findById() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder, savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemRepository.save(orderLineItem);

        // when
        final OrderLineItem foundOrderLineItem = orderLineItemRepository.findById(saved.getSeq())
                .get();

        // then
        assertThat(foundOrderLineItem).usingRecursiveComparison()
                .isEqualTo(saved);
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

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder, savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemRepository.save(orderLineItem);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .contains(saved.getSeq())
        );
    }

    @Test
    @DisplayName("주문 id로 모든 주문 항목을 조회한다")
    void findByAllByOrderId() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final Order order = OrderFixtures.MEAL_ORDER.createWithOrderTableId(savedOrderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        final Menu menu = MenuFixtures.TWO_CHICKEN_COMBO.create();
        final Menu savedMenu = menuRepository.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem(null, savedOrder, savedMenu.getId(), 2);
        final OrderLineItem saved = orderLineItemRepository.save(orderLineItem);

        // when
        final List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());

        // then
        assertAll(
                () -> assertThat(orderLineItems).hasSize(1),
                () -> assertThat(orderLineItems).extracting("seq")
                        .containsExactly(saved.getSeq())
        );
    }
}

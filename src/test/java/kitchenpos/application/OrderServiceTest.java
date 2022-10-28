package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderCompletionException;
import kitchenpos.exception.OrderLineItemEmptyException;
import kitchenpos.exception.OrderLineItemSizeException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.OrderLineItemFixtures;
import kitchenpos.fixtures.OrderTableFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, 1L, 2);

        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable notEmptyOrderTable = orderTableRepository.save(orderTable);

        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(
                notEmptyOrderTable.getId(), orderLineItem);

        // when
        final Order saved = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(saved.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(saved.getOrderTableId()).isEqualTo(notEmptyOrderTable.getId()),
                () -> assertThat(saved.getOrderLineItems()).extracting("orderId")
                        .containsOnly(saved.getId())
        );
    }

    @Test
    @DisplayName("주문 항목이 비어있는 상태로 주문을 생성하면 예외가 발생한다")
    void createExceptionEmptyOrderLineItems() {
        // given
        final Order order = OrderFixtures.COOKING_ORDER.create();

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴들이 실제 존재하지 않은 메뉴로 주문을 생성하면 예외가 발생한다")
    void createExceptionWrongOrderLineItems() {
        // given
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, -1L, 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(1L, orderLineItem);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(OrderLineItemSizeException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않은 테이블이면 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotExistOrderTable() {
        // given
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, 1L, 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(-1L, orderLineItem);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 주문을 등록할 수 없는 상태로 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotEmptyOrderTable() {
        // given
        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(null, 1L, 2);
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableIdAndOrderLineItems(1L, orderLineItem);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(TableEmptyException.class);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixtures.createWithGuests(null, 2);
        final OrderTable notEmptyOrderTable = orderTableRepository.save(orderTable);

        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableId(notEmptyOrderTable.getId());
        final Order savedOrder = orderDao.save(order);

        final OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedOrder.getId(), 1L, 2);
        orderLineItemDao.save(orderLineItem);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertAll(
                () -> assertThat(orders).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orders).extracting("id")
                        .contains(savedOrder.getId()),
                () -> assertThat(orders).extracting("orderLineItems")
                        .isNotEmpty()
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void changeOrderStatus() {
        // given
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableId(1L);
        final Order saved = orderDao.save(order);
        final Order orderInMeal = OrderFixtures.MEAL_ORDER.create();

        // when
        final Order changedOrder = orderService.changeOrderStatus(saved.getId(), orderInMeal);

        // when, then
        assertAll(
                () -> assertThat(changedOrder.getId()).isEqualTo(saved.getId()),
                () -> assertThat(changedOrder.getOrderedTime()).isEqualTo(saved.getOrderedTime()),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @Test
    @DisplayName("변경하려는 주문이 존재하지 않을 때 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    void changeOrderStatusExceptionWrongOrderId() {
        // given
        final Order order = OrderFixtures.COOKING_ORDER.createWithOrderTableId(1L);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, order))
                .isExactlyInstanceOf(NotFoundOrderException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태에서 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    void changeOrderStatusExceptionAlreadyCompletion() {
        // given
        final Order order = OrderFixtures.COMPLETION_ORDER.createWithOrderTableId(1L);
        final Order completionOrder = orderDao.save(order);
        final Order orderInMeal = OrderFixtures.MEAL_ORDER.create();

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrder.getId(), orderInMeal))
                .isExactlyInstanceOf(OrderCompletionException.class);
    }
}

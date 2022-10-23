package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable notEmptyOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(notEmptyOrderTable.getId());

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
        final Order order = new Order();

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴들이 실제 존재하지 않은 메뉴로 주문을 생성하면 예외가 발생한다")
    void createExceptionWrongOrderLineItems() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(-1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않은 테이블이면 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotExistOrderTable() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(-1L);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 주문을 등록할 수 없는 상태로 주문을 생성할 때 예외가 발생한다")
    void createExceptionNotEmptyOrderTable() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(1L);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 주문을 조회한다")
    void list() {
        // given
        final Order order = new Order();
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable notEmptyOrderTable = orderTableDao.save(orderTable);
        order.setOrderTableId(notEmptyOrderTable.getId());

        final Order saved = orderService.create(order);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertAll(
                () -> assertThat(orders).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(orders).usingRecursiveFieldByFieldElementComparator()
                        .extracting("id")
                        .contains(saved.getId()),
                () -> assertThat(orders).usingRecursiveFieldByFieldElementComparator()
                        .usingElementComparatorOnFields("orderLineItems")
                        .isNotEmpty()
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void changeOrderStatus() {
        // given
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order saved = orderDao.save(order);

        final Order orderInMeal = new Order();
        orderInMeal.setOrderStatus(OrderStatus.MEAL.name());

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
        final Order order = new Order();

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태에서 주문의 상태를 변경하려고 하면 예외가 발생한다.")
    void changeOrderStatusExceptionAlreadyCompletion() {
        // given
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order completionOrder = orderDao.save(order);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, completionOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}

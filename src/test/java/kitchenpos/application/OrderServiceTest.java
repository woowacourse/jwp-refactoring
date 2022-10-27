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

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 항목이 빈 경우, 예외를 발생한다")
    @Test
    void empty_order_line_items_exception() {
        final Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 없는 경우, 예외를 발생한다")
    @Test
    void does_not_exist_order_table_exception() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 경우, 예외를 발생한다")
    @Test
    void empty_order_table_exception() {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(1L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        final OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setEmpty(false);
        final OrderTable orderTable = orderTableDao.save(orderTableRequest);

        final Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTable.getId());

        // when
        final Order createdOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(createdOrder.getId()).isNotNull(),
                () -> assertThat(createdOrder.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(createdOrder.getOrderStatus()).isEqualToIgnoringCase(OrderStatus.COOKING.name()),
                () -> assertThat(createdOrder.getOrderLineItems()).usingElementComparatorOnFields("menuId", "quantity")
                        .contains(orderLineItem)
        );
    }

    @DisplayName("주문 전체 목록을 조회한다")
    @Test
    void findAll() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);

        final OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setEmpty(false);
        final OrderTable orderTable = orderTableDao.save(orderTableRequest);

        final Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderTableId(orderTable.getId());

        orderService.create(order);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경할 때 주문 번호가 없는 경우, 예외를 발생한다")
    @Test
    void not_found_order_exception() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경할 때 주문 상태가 completion인 경우, 예외를 발생한다")
    @Test
    void order_state_completion_exception() {
        // given
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        final Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void change_order_status() {
        // given
        final Order orderRequest = new Order();
        orderRequest.setOrderTableId(1L);
        orderRequest.setOrderStatus(OrderStatus.COOKING.name());
        orderRequest.setOrderedTime(LocalDateTime.now());
        final Order savedOrder = orderDao.save(orderRequest);

        final Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}

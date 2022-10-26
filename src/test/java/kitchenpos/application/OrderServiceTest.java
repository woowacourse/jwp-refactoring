package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService sut;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Test
    @DisplayName("주문 항목이 없으면 주문을 생성할 수 없다")
    void throwExceptionWhenNoOrderLineItems() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);

        // when && then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 포함된 메뉴는 중복될 수 없다")
    void throwExceptionWhenDuplicateMenu() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1);

        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setMenuId(1L);
        orderLineItem2.setQuantity(2);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem1, orderLineItem2);

        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(orderLineItems);

        // when && then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다")
    void throwExceptionWhenOrderTableDoesNotExist() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(0L);
        order.setOrderLineItems(orderLineItems);

        // when && then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없다.")
    void throwExceptionWhenOrderTableIsEmpty() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        Long orderTableId = orderTableDao.save(orderTable).getId();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        // when && then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성한다")
    void createOrder() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);
        Long orderTableId = orderTableDao.save(orderTable).getId();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        // when
        Order savedOrder = sut.create(order);

        // then
        assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTableId);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
        assertThatOrderIdIsSet(savedOrder.getOrderLineItems(), savedOrder.getId());
    }

    @Test
    @DisplayName("주문 목록을 조회한다")
    void listOrders() {
        List<Order> expected = orderDao.findAll();

        List<Order> actual = sut.list();

        assertThat(actual).hasSize(expected.size());
        for (Order order : actual) {
            assertThatOrderIdIsSet(order.getOrderLineItems(), order.getId());
        }
    }

    @Test
    @DisplayName("입력받은 Id에 해당하는 주문이 존재하지 않으면 주문 상태를 변경할 수 없다")
    void throwExceptionWhenOrderDoesNotExist() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        // when && then
        assertThatThrownBy(() -> sut.changeOrderStatus(0L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 이미 완료 상태인 주문은 상태를 변경할 수 없다")
    void throwExceptionWhenTryToChangeCompletedOrder() {
        // given
        Order savedOrder = createValidOrder();

        Order completeStatusOrder = new Order();
        completeStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        sut.changeOrderStatus(savedOrder.getId(), completeStatusOrder);

        Order mealStatusOrder = new Order();
        mealStatusOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when && then
        assertThatThrownBy(() -> sut.changeOrderStatus(savedOrder.getId(), mealStatusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        // given
        Order savedOrder = createValidOrder();

        Order completeStatusOrder = new Order();
        completeStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        Order changedOrder = sut.changeOrderStatus(savedOrder.getId(), completeStatusOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThat(changedOrder).usingRecursiveComparison()
                .ignoringFields("orderStatus")
                .isEqualTo(savedOrder);
    }

    private void assertThatOrderIdIsSet(List<OrderLineItem> orderLineItems, Long orderId) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            assertThat(orderLineItem.getOrderId()).isEqualTo(orderId);
        }
    }

    private Order createValidOrder() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);
        Long orderTableId = orderTableDao.save(orderTable).getId();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        List<OrderLineItem> orderLineItems = List.of(orderLineItem);

        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);

        return sut.create(order);
    }
}

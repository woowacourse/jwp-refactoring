package kitchenpos.integration.order;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderDao orderDao;

    private OrderTable savedOrderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable(2, false);

        savedOrderTable = tableService.create(orderTable);

        orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(1);
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder)
            .usingRecursiveComparison()
            .ignoringFields("id", "orderedTime", "orderLineItems.seq")
            .isEqualTo(order);
    }

    @DisplayName("주문의 주문 항목이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingMenuInOrderLineItems_Fail() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 항목에 메뉴가 하나라도 적혀있지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderLineItems_Fail() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(100L);
        orderLineItem.setQuantity(1);

        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderTable_Fail() {
        // given
        Order order = new Order(100L, COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 비어 있으면(손님이 없으면) 등록할 수 없다.")
    @Test
    void create_NonExistingGuestsInOrderTable_Fail() {
        // given
        Order order = new Order(1L, COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        // when
        // then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void read_Valid_Success() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).isNotEmpty();
    }


    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus_Valid_Success() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        Order savedOrder = orderService.create(order);

        Order findOrder = orderDao.findById(savedOrder.getId())
            .orElseThrow(IllegalArgumentException::new);
        findOrder.changeStatus(MEAL.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), findOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(findOrder.getOrderStatus());
    }

    @DisplayName("주문 상태는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatus_NonExistingOrder_Fail() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        Order savedOrder = orderService.create(order);

        Order findOrder = orderDao.findById(savedOrder.getId())
            .orElseThrow(IllegalArgumentException::new);
        findOrder.changeStatus(MEAL.name());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, findOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 `계산 완료`면 변경할 수 없다.")
    @Test
    void changeOrderStatus_InvalidOrderStatus_Fail() {
        // given
        Order order = new Order(savedOrderTable.getId(), COOKING.name(), LocalDateTime.now());
        order.add(Collections.singletonList(orderLineItem));

        Order savedOrder = orderService.create(order);

        Order findOrder1 = orderDao.findById(savedOrder.getId())
            .orElseThrow(IllegalArgumentException::new);
        findOrder1.changeStatus(COMPLETION.name());

        orderService.changeOrderStatus(savedOrder.getId(), findOrder1);

        Order findOrder2 = orderDao.findById(savedOrder.getId())
            .orElseThrow(IllegalArgumentException::new);
        findOrder2.changeStatus(MEAL.name());

        // when
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), findOrder2))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

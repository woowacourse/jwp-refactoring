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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderDao orderDao;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);

        tableService.create(orderTable);

        orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(2L);
        orderLineItem.setQuantity(1);
    }

    @DisplayName("주문을 등록한다.")
    @Nested
    class Create {

        @DisplayName("주문을 등록할 수 있다.")
        @Test
        void create_Valid_Success() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            // when
            Order savedOrder = orderService.create(order);

            // then
            assertThat(savedOrder)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems.seq")
                .isEqualTo(order);
        }

        @DisplayName("주문의 주문 항목이 존재하지 않으면 등록할 수 없다.")
        @Test
        void create_NonExistingMenuInOrderLineItems_Fail() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.emptyList());

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
            orderLineItem.setMenuId(8L);
            orderLineItem.setQuantity(1);

            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않으면 등록할 수 없다.")
        @Test
        void create_NonExistingOrderTable_Fail() {
            // given
            Order order = new Order();
            order.setOrderTableId(100L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 비어 있으면(손님이 없으면) 등록할 수 없다.")
        @Test
        void create_NonExistingGuestsInOrderTable_Fail() {
            // given
            Order order = new Order();
            order.setOrderTableId(1L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Nested
    class Read {

        /**
         * 여기서 테스트 격리가 잘 안됨
         * Update 클래스의 메소드들이 영향을 끼치는 상황
         */
        @DisplayName("주문의 목록을 조회할 수 있다.")
        @Test
        void read_Valid_Success() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            orderService.create(order);

            // when
            List<Order> orders = orderService.list();

            // then
            assertThat(orders).isNotEmpty();
        }
    }

    @DisplayName("주문 상태를 변경한다.")
    @Nested
    class Update {

        @DisplayName("주문 상태를 변경할 수 있다.")
        @Test
        void changeOrderStatus_Valid_Success() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            Order savedOrder = orderService.create(order);

            Order findOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow(IllegalArgumentException::new);
            findOrder.setOrderStatus(MEAL.name());

            // when
            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), findOrder);

            // then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(findOrder.getOrderStatus());
        }

        @DisplayName("주문 상태는 주문이 존재하지 않으면 변경할 수 없다.")
        @Test
        void changeOrderStatus_NonExistingOrder_Fail() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            Order savedOrder = orderService.create(order);

            Order findOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow(IllegalArgumentException::new);
            findOrder.setOrderStatus(MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(100L, findOrder))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 `계산 완료`면 변경할 수 없다.")
        @Test
        void changeOrderStatus_InvalidOrderStatus_Fail() {
            // given
            Order order = new Order();
            order.setOrderTableId(9L);
            order.setOrderStatus(COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            Order savedOrder = orderService.create(order);

            Order findOrder1 = orderDao.findById(savedOrder.getId())
                .orElseThrow(IllegalArgumentException::new);
            findOrder1.setOrderStatus(COMPLETION.name());

            orderService.changeOrderStatus(1L, findOrder1);

            Order findOrder2 = orderDao.findById(savedOrder.getId())
                .orElseThrow(IllegalArgumentException::new);
            findOrder2.setOrderStatus(MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), findOrder2))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문을 생성한다")
    @Nested
    class CreateTest {

        Long orderTableId = 1L;

        @BeforeEach
        void setUp() {
            changeTableToNotEmpty(orderTableId);
        }

        @DisplayName("주문을 생성하면 ID가 할당된 Order객체가 반환된다")
        @Test
        void create() {
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 3));
            Order order = new Order(orderTableId, orderLineItems);

            Order actual = orderService.create(order);
            assertThat(actual).isNotNull();
        }

        @DisplayName("orderLineItems이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyOrderLineItems() {
            Order order = new Order(1L, List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("orderLineItems에 존재하지 않는 메뉴가 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistMenu() {
            Long notExistId = 0L;
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(notExistId, 3));
            Order order = new Order(1L, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistTable() {
            Long notExistId = 0L;
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 3));
            Order order = new Order(notExistId, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            Long emptyTableId = 3L;
            List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 3));
            Order order = new Order(emptyTableId, orderLineItems);

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {
        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(0);
    }

    @DisplayName("주문 상태를 변경한다")
    @Nested
    class ChangeOrderStatusTest {

        @DisplayName("주문 상태를 변경한다")
        @Test
        void changeOrderStatus() {
            Order order = createOrder();
            String expectedOrderStatus = OrderStatus.COMPLETION.name();

            Order orderToChangeCompleteStatus = createOrder();
            orderToChangeCompleteStatus.setOrderStatus(expectedOrderStatus);
            Order changedOrder = orderService.changeOrderStatus(order.getId(), orderToChangeCompleteStatus);

            assertThat(changedOrder.getOrderStatus()).isEqualTo(expectedOrderStatus);
        }

        @DisplayName("존재하지 않는 주문일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrder() {
            Long notExistId = 0L;
            Order orderToUpdate = createOrder();

            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistId, orderToUpdate))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Complete상태의 주문은 변경할 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfCompletedOrder() {
            Order order = createOrder();

            Order orderToChangeCompleteStatus = createOrder();
            orderToChangeCompleteStatus.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(order.getId(), orderToChangeCompleteStatus);

            Order orderToChange = createOrder();
            orderToChange.setOrderStatus(OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderToChange))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Order createOrder() {
        Long orderTableId = 1L;
        changeTableToNotEmpty(orderTableId);

        List<OrderLineItem> orderLineItems = List.of(new OrderLineItem(1L, 3));
        Order order = new Order(orderTableId, orderLineItems);

        return orderService.create(order);
    }

    void changeTableToNotEmpty(Long tableId) {
        OrderTable orderTable = orderTableDao.findById(tableId)
                .orElseThrow();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);

        orderTableDao.save(orderTable);
    }
}

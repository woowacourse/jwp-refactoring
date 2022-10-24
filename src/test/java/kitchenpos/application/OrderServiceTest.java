package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;

    @Nested
    @DisplayName("주문 생성 로직 테스트")
    class create {

        @Test
        @DisplayName("주문을 생성한다.")
        void create() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            final Long tableId = tableService.create(orderTable).getId();
            final Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of(new OrderLineItem(1L, 1L)));

            final Order actual = orderService.create(order);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(tableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        @DisplayName("orderLineItems가 비어있는데 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_emptyOrderLineItems() {
            final Long tableId = tableService.create(new OrderTable()).getId();
            final Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("존재하지 않는 메뉴로 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_notExistMenuId() {
            final Long tableId = tableService.create(new OrderTable()).getId();
            final Order order = new Order();
            order.setOrderTableId(tableId);
            final Long notExistMenuId = 999999L;
            order.setOrderLineItems(List.of(new OrderLineItem(notExistMenuId, 1L)));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("존재하지 않는 테이블에 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_notExistTable() {
            final Long tableId = 999999L;
            final Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of(new OrderLineItem(1L, 1L)));

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블이 비어있는데 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_emptyTable() {
            final OrderTable orderTable = tableService.create(new OrderTable());
            final Long tableId = orderTable.getId();
            orderTable.setEmpty(true);
            final Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of());

            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    @DisplayName("주문 내역을 반환한다.")
    void list() {
        final Order order = new Order();
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        order.setOrderTableId(tableService.create(orderTable).getId());
        order.setOrderLineItems(List.of(new OrderLineItem(1L, 1L)));
        orderService.create(order);
        orderService.create(order);

        final List<Order> actual = orderService.list();

        assertThat(actual).hasSizeGreaterThan(1);
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class changeOrderStatus {

        @Test
        @DisplayName("주문 상태를 변경한다.")
        void changeOrderStatus() {
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            final Long tableId = tableService.create(orderTable).getId();
            Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of(new OrderLineItem(1L, 1L)));
            final Long orderId = orderService.create(order).getId();

            final Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL.name());

            final Order actual = orderService.changeOrderStatus(orderId, newOrder);

            assertAll(
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(tableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
            );
        }

        @Test
        @DisplayName("존재하지 않는 주문의 상태를 변경하려는 경우 예외를 발생 시킨다.")
        void changeOrderStatus_notExistOrder(){
            tableService.create(new OrderTable());
            final Long notExistOrderId = 999999L;

            final Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, newOrder))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Completion인 주문의 상태를 변경하려는 경우 예외를 발생 시킨다.")
        void changeOrderStatus_CompletionOrder(){
            final OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            final Long tableId = tableService.create(orderTable).getId();

            Order order = new Order();
            order.setOrderTableId(tableId);
            order.setOrderLineItems(List.of(new OrderLineItem(1L, 1L)));
            final Order savedOrder = orderService.create(order);
            final Long orderId = savedOrder.getId();

            final Order newOrder = new Order();
            newOrder.setOrderStatus(OrderStatus.COMPLETION.name());

            orderService.changeOrderStatus(orderId, newOrder);
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, newOrder))
                            .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

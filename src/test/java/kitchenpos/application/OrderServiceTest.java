package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class OrderServiceTest {

    public static final Long INVALID_ID = 100L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreate {

        @DisplayName("[성공] 새로운 주문 등록")
        @Test
        void create_success() {
            // given
            Order order = newOrder();

            // when
            Order createdOrder = orderService.create(order);

            // then
            assertThat(createdOrder.getId()).isNotNull();
            assertThat(createdOrder).extracting("orderTableId", "orderStatus")
                .contains(order.getOrderTableId(), "COOKING");
            assertThat(createdOrder.getOrderLineItems().stream()
                .map(OrderLineItem::getSeq)
                .filter(Objects::nonNull)
                .count())
                .isEqualTo(order.getOrderLineItems().size());
        }

        @DisplayName("[실패] 주문항목이 비어있을 경우 예외 발생")
        @Test
        void create_emptyOrderLineItems_ExceptionThrwon() {
            // given
            Order order = newOrder();
            order.setOrderLineItems(Collections.emptyList());

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문항목의 메뉴가 존재하지 않을 경우 예외 발생")
        @Test
        void create_InvalidMenu_ExceptionThrown() {
            // given
            OrderLineItem invalidMenuItem = newOrderLineItem();
            invalidMenuItem.setMenuId(INVALID_ID);

            Order order = newOrder();
            order.setOrderLineItems(Collections.singletonList(invalidMenuItem));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문항목의 주문 테이블이 존재하지 않을 경우 예외 발생")
        @Test
        void create_InvalidOrderTable_ExceptionThrown() {
            // given
            Order order = newOrder();
            order.setOrderTableId(INVALID_ID);

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문항목의 주문 테이블이 비어있을 경우 예외 발생")
        @Test
        void create_EmptyOrderTable_ExceptionThrown() {
            // given
            Order order = newOrder();
            order.setOrderTableId(emptyOrNotOrderTable(true).getId());

            // when
            // then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderChangeStatus {
        @DisplayName("[성공] 주문 상태 변경")
        @Test
        void changeOrderStatus_Success() {
            // given
            Order order = orderService.create(newOrder());
            Order orderWithTargetStatus = newOrder();
            orderWithTargetStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when
            Order result = orderService.changeOrderStatus(order.getId(), orderWithTargetStatus);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }

        @DisplayName("[실패] 주문 ID가 유효하지 않으면 예외 발생")
        @Test
        void changeOrderStatus_InvalidOrderId_ExceptionThrown() {
            // given
            Order orderWithTargetStatus = newOrder();
            orderWithTargetStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(INVALID_ID, orderWithTargetStatus))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문 ID가 유효하지 않으면 예외 발생")
        @Test
        void changeOrderStatus_CompletionOrderStatus_ExceptionThrown() {
            // given
            Order completedOrder = newOrder();
            completedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

            Long completedOrderId =
                orderService.changeOrderStatus(
                    orderService.create(newOrder()).getId(), completedOrder
                ).getId();

            Order orderWithTargetStatus = newOrder();
            orderWithTargetStatus.setOrderStatus(OrderStatus.MEAL.name());

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(completedOrderId, orderWithTargetStatus))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("[성공] 주문 항목 전체 조회")
    @Test
    void list_Success() {
        // given
        int previousSize = orderService.list().size();
        orderService.create(newOrder());

        // when
        List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(previousSize + 1);
    }

    private Order newOrder() {
        Order order = new Order();
        order.setOrderTableId(emptyOrNotOrderTable(false).getId());
        order.setOrderLineItems(Collections.singletonList(newOrderLineItem()));

        return order;
    }

    private OrderLineItem newOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        return orderLineItem;
    }

    private OrderTable emptyOrNotOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return tableService.create(orderTable);
    }
}

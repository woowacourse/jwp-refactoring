package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Menu menu = new Menu();

    @DisplayName("주문 등록 시 주문테이블 등록 여부 확인")
    @Test
    void setOrderTable() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(orderTable, orderLineItems);

        assertAll(
            () -> assertThat(order.getOrderTable()).isEqualTo(orderTable),
            () -> assertThat(order.getOrderTable().getOrders()).containsOnly(order)
        );
    }

    @DisplayName("주문 등록 시 주문항목 등록 여부 확인")
    @Test
    void setOrderLineItemsTest() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem1 = createOrderLineItem(menu);
        OrderLineItem orderLineItem2 = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        Order order = createOrder(orderTable, orderLineItems);

        assertAll(
            () -> assertThat(order.getOrderLineItems().get(0).getOrder()).isEqualTo(order),
            () -> assertThat(order.getOrderLineItems().get(1).getOrder()).isEqualTo(order)
        );
    }

    @DisplayName("[예외] 빈 테이블에 대한 주문 생성")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> Order.builder()
                .orderTable(orderTable)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 주문항목 없이 주문 생성")
    @Test
    void create_Fail_With_Empty_OrderLineItems() {
        OrderTable orderTable = createOrderTable(false);

        assertThatThrownBy(
            () -> Order.builder()
                .orderTable(orderTable)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(orderTable, orderLineItems);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("[예외] 이미 완료된 주문의 상태 변경")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        OrderTable orderTable = createOrderTable(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(orderTable, orderLineItems);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(
            () -> order.changeOrderStatus(OrderStatus.COMPLETION)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
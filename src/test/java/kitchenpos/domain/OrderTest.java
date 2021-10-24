package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Order 엔티티 단위 테스트")
class OrderTest {

    @DisplayName("Order 생성시 OrderTable이 Empty하면 생성 예외가 발생한다.")
    @Test
    void newOrder_OrderTableEmpty_Exception() {
        // given
        OrderTable orderTable = new OrderTable(10, true);

        // when, then
        assertThatCode(() -> new Order(orderTable, OrderStatus.COMPLETION))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("OrderTable이 비어있는 상태입니다.");
    }

    @DisplayName("Order 생성시 OrderTable이 Empty하지 않으면 정상 생성된다.")
    @Test
    void newOrder_OrderTableNotEmpty_Success() {
        // given
        OrderTable orderTable = new OrderTable(10, false);

        // when, then
        assertThatCode(() -> new Order(orderTable, OrderStatus.COMPLETION))
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderStatus가 Completion이면 변경이 불가능하다.")
    @Test
    void changeOrderStatus_Completion_Exception() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION);

        // when, then
        assertThatCode(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Order 상태를 변경할 수 없는 상황입니다.");
    }

    @DisplayName("OrderStatus가 Completion이 아니면 변경이 가능하다.")
    @ParameterizedTest
    @EnumSource(names = {"COOKING", "MEAL"})
    void changeOrderStatus_NotCompletion_Success(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        Order order = new Order(orderTable, orderStatus);

        // when
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThat(order.isNotCompleted()).isFalse();
    }

    @DisplayName("OrderLineItems 업데이트시 비어있는 컬렉션이면 예외가 발생한다.")
    @Test
    void updateOrderLineItems_Empty_Exception() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION);

        // when, then
        assertThatCode(() -> order.updateOrderLineItems(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문에 포함된 주문 항목이 없습니다.");
    }

    @DisplayName("OrderLineItems 업데이트시 비어있는 컬렉션이 아니면 정상 변경된다.")
    @Test
    void updateOrderLineItems_NotEmpty_Success() {
        // given
        OrderTable orderTable = new OrderTable(10, false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION);

        // when
        order.updateOrderLineItems(Arrays.asList(new OrderLineItem()));

        // then
        assertThat(order.getOrderLineItems()).hasSize(1);
    }
}

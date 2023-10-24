package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private OrderValidator orderValidator;

    @DisplayName("주문 테이블이 없을 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsNull() {
        //given
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(OrderLineItem.create(1L, 1L)));

        //when then
        assertThatThrownBy(() -> Order.create(null, orderLineItems, orderValidator))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("주문 테이블은 null일 수 없습니다.");
    }

    @DisplayName("주문 테이블이 주문할 수 없는 상태일 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsEmpty() {
        //given
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(OrderLineItem.create(1L, 1L)));
        doThrow(new IllegalArgumentException("주문할 수 없는 상태의 테이블이 존재합니다."))
                .when(orderValidator).validateOrderTable(any());

        //when then
        assertThatThrownBy(() -> Order.create(1L, orderLineItems, orderValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문할 수 없는 상태의 테이블이 존재합니다.");
    }

    @DisplayName("주문이 생성되면, COOKING 상태가 된다.")
    @Test
    void createOrderSuccessTest_HavingCookingStatus() {
        //given
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(OrderLineItem.create(1L, 1L)));

        //when
        Order order = Order.create(1L, orderLineItems, orderValidator);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태가 COMPLETION이면, 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(OrderLineItem.create(1L, 1L)));
        Order order = Order.create(1L, orderLineItems, orderValidator);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Completion 상태일 경우, 주문 상태를 변경할 수 없습니다.");
    }

    @ParameterizedTest(name = "주문 상태가 COMPLETION이 아니면, 상태를 변경할 수 있다.")
    @EnumSource(mode = Mode.INCLUDE, names = {"MEAL", "COOKING"})
    void changeOrderStatusSuccessTest_ByOrderStatusIsNotCompletion(OrderStatus orderStatus) {
        //given
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(OrderLineItem.create(1L, 1L)));

        Order order = Order.create(1L, orderLineItems, orderValidator);
        order.changeOrderStatus(orderStatus);

        //when
        order.changeOrderStatus(orderStatus);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

}

package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderTest {

    @DisplayName("주문 메뉴(내역)가 비어있을 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderLineItemIsEmpty() {
        //when then
        assertThatThrownBy(() -> Order.createNewOrder(new OrderTable(), Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 없을 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsNull() {
        //when then
        assertThatThrownBy(() -> Order.createNewOrder(null, List.of(new OrderLineItem())))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주문 테이블이 주문할 수 없는 상태일 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsEmpty() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        //when then
        assertThatThrownBy(() -> Order.createNewOrder(orderTable, List.of(new OrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 생성되면, COOKING 상태가 된다.")
    @Test
    void createOrderSuccessTest_HavingCookingStatus() {
        //when
        Order order = Order.createNewOrder(new OrderTable(), List.of(new OrderLineItem()));
        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태가 COMPLETION이면, 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        Order order = Order.createNewOrder(new OrderTable(), List.of(new OrderLineItem()));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "주문 상태가 COMPLETION이 아니면, 상태를 변경할 수 있다.")
    @EnumSource(mode = Mode.INCLUDE, names = {"MEAL", "COOKING"})
    void changeOrderStatusSuccessTest_ByOrderStatusIsNotCompletion(OrderStatus orderStatus) {
        //given
        Order order = Order.createNewOrder(new OrderTable(), List.of(new OrderLineItem()));
        order.changeOrderStatus(orderStatus);

        //when
        order.changeOrderStatus(orderStatus);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

}

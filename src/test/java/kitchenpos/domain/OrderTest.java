package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderTest {

    @DisplayName("주문 메뉴(내역)가 비어있을 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderLineItemIsEmpty() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);

        //when then
        assertThatThrownBy(() -> Order.create(orderTable, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 메뉴는 1개 이상 존재해야 합니다.");
    }

    @DisplayName("주문 테이블이 없을 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsNull() {
        //when then
        assertThatThrownBy(() -> Order.createWithEmptyOrderLinItems(null))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("주문 테이블이 주문할 수 없는 상태일 경우, 생성할 수 없다.")
    @Test
    void createOrderFailTest_ByOrderTableIsEmpty() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.TRUE);

        //when then
        assertThatThrownBy(() -> Order.createWithEmptyOrderLinItems(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문할 수 없는 상태의 테이블이 존재합니다.");
    }

    @DisplayName("주문이 생성되면, COOKING 상태가 된다.")
    @Test
    void createOrderSuccessTest_HavingCookingStatus() {
        //when
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);
        Order order = Order.createWithEmptyOrderLinItems(orderTable);
        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태가 COMPLETION이면, 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);
        Order order = Order.createWithEmptyOrderLinItems(orderTable);
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
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, Boolean.FALSE);

        Order order = Order.createWithEmptyOrderLinItems(orderTable);
        order.changeOrderStatus(orderStatus);

        //when
        order.changeOrderStatus(orderStatus);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

}

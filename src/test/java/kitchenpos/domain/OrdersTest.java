package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdersTest {

    @DisplayName("주문 생성 - 실패 - 빈 테이블인 경우")
    @Test
    void createFailure() {
        //given
        OrderTable orderTable = new OrderTable(3, true);
        //when
        //then
        assertThatThrownBy(() -> new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = new OrderTable(3, false);
        Orders cookingOrders = new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        cookingOrders.changeOrderStatus(OrderStatus.MEAL);

        assertThat(cookingOrders.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경 - 실패 - 이미 완료된 주문인 경우")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        OrderTable orderTable = new OrderTable(3, false);
        Orders completedOrders = new Orders(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(() -> completedOrders.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

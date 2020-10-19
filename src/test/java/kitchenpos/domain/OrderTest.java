package kitchenpos.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 생성")
    @Test
    public void createOrder() {
        final OrderTable orderTable = new OrderTable(1L, null, 4, false);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Lists.newArrayList());

        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태 변경 실패 - 식사 완료")
    @Test
    public void changeOrderStatusFail() {
        final OrderTable orderTable = new OrderTable(1L, null, 4, false);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Lists.newArrayList());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    public void changeOrderStatus() {
        final OrderTable orderTable = new OrderTable(1L, null, 4, false);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), Lists.newArrayList());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

}
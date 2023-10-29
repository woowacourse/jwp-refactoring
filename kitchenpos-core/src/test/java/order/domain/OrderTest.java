package order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

class OrderTest {

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreateTest {

        @DisplayName("주문을 생성한다.")
        @Test
        void orderCreateTest() {
            //given & when
            final Order order = new Order(null, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.emptyList());

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(order.getId()).isNull();
                softly.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
            });
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderStatusChangeTest {

        @DisplayName("주문 상태를 변경한다.")
        @Test
        void orderStatusChangeTest() {
            //given
            final Order order = new Order(null, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.emptyList());
            final String afterStatus = OrderStatus.COMPLETION.name();

            //when
            order.changeOrderStatus(afterStatus);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(order.getOrderStatus()).isEqualTo(afterStatus);
            });
        }

        @DisplayName("주문 상태가 완료면 실패한다.")
        @Test
        void orderStatusChangeFailWhenOrderStatusCompletion() {
            //given
            final Order order = new Order(null, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());

            // when & then
            Assertions.assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Nested
    @DisplayName("")
    class next {

        @Test
        void testTransitionFromCookingToMeal() {
            // given
            OrderStatus initialStatus = OrderStatus.COOKING;

            // when
            OrderStatus nextStatus = initialStatus.next();

            // then
            assertThat(nextStatus).isEqualTo(OrderStatus.MEAL);

        }

        @Test
        void testTransitionFromMealToCompletion() {
            // given
            OrderStatus initialStatus = OrderStatus.MEAL;

            // when
            OrderStatus nextStatus = initialStatus.next();

            // then
            assertThat(nextStatus).isEqualTo(OrderStatus.COMPLETION);
        }

        @Test
        void testNoTransitionFromCompletion() {
            // given
            OrderStatus initialStatus = OrderStatus.COMPLETION;

            // when
            // then
            assertThatThrownBy(initialStatus::next)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 상태를 변경할 수 없습니다.");
            ;
        }

    }
}

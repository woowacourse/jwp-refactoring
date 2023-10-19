package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.domain.exception.OrderExceptionType.ORDER_IS_ALREADY_COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;
import kitchenpos.domain.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Nested
    @DisplayName("OrderStatus를 바꾼다.")
    class ChangeOrderStatus {

        @Test
        @DisplayName("성공적으로 바꾼다..")
        void success() {
            final Order order = new Order(1L, OrderStatus.MEAL, LocalDateTime.now(), Map.of());

            order.changeOrderStatus(COMPLETION);

            assertThat(order.getOrderStatus())
                .isEqualTo(COMPLETION);
        }

        @Test
        @DisplayName("order의 상태가 이미 completion인데, 바꿀 수 없다.")
        void throwExceptionOrderIsAlreadyCompletion() {
            final Order order = new Order(1L, COMPLETION, LocalDateTime.now(), Map.of());

            assertThatThrownBy(() -> order.changeOrderStatus(COMPLETION))
                .isInstanceOf(OrderException.class)
                .hasMessage(ORDER_IS_ALREADY_COMPLETION.getMessage());
        }
    }

    @Nested
    @DisplayName("Order가 이미 종료되었는지 확인하는 기능 추가")
    class AlreadyCompletion {

        @Test
        @DisplayName("이미 종료된 경우")
        void trueCase() {
            final Order order = new Order(1L, COMPLETION, LocalDateTime.now(), Map.of());

            assertTrue(order.isAlreadyCompletion());
        }

        @Test
        @DisplayName("이미 종료되지 않은 경우 경우")
        void falseCase() {
            final Order order = new Order(1L, MEAL, LocalDateTime.now(), Map.of());

            assertFalse(order.isAlreadyCompletion());
        }
    }

    @Nested
    @DisplayName("Order가 종료가 안됬는지 확인하는 기능 추가")
    class IsAlreadyCompletion {

        @Test
        @DisplayName("이미 종료되지 않은 경우 경우")
        void trueCase() {
            final Order order = new Order(1L, MEAL, LocalDateTime.now(), Map.of());

            assertTrue(order.isNotAlreadyCompletion());
        }

        @Test
        @DisplayName("이미 종료된 경우")
        void falseCase() {
            final Order order = new Order(1L, COMPLETION, LocalDateTime.now(), Map.of());

            assertFalse(order.isNotAlreadyCompletion());
        }
    }
}

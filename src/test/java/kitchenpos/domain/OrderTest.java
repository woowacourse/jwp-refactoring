package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.OrderAlreadyCompletionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Order 단위 테스트")
class OrderTest {

    @DisplayName("Order를 생성할 때 OrderTable이 Null이면 예외가 발생한다.")
    @Test
    void orderTableNullException() {
        // when, then
        assertThatThrownBy(() -> new Order(null))
            .isExactlyInstanceOf(InvalidOrderException.class);
    }

    @DisplayName("Order의 상태를 변경할 때")
    @Nested
    class ChangeStatus {

        @DisplayName("이미 Completion인 경우 예외가 발생한다.")
        @Test
        void alreadyCompletionException() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);
            order.changeStatus(COMPLETION);

            // when, then
            assertThatThrownBy(() -> order.changeStatus(COMPLETION))
                .isExactlyInstanceOf(OrderAlreadyCompletionException.class);
        }

        @DisplayName("Completion 상태가 아닌 경우 상태가 변경된다.")
        @Test
        void success() {
            // given
            OrderTable orderTable = new OrderTable(5, true);
            Order order = new Order(orderTable);
            order.changeStatus(MEAL);

            // when, then
            assertThatCode(() -> order.changeStatus(COMPLETION))
                .doesNotThrowAnyException();
        }
    }
}

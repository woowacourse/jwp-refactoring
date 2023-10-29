package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import kitchenpos.core.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderStatusTest {
    @Test
    @DisplayName("COOKING 상태의 OrderStatus에서 다음 단계로 이동하면 MEAL 상태가 된다.")
    void cooking() {
        assertThat(OrderStatus.COOKING.transitionTo(OrderStatus.MEAL))
                .isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("MEAL 상태의 OrderStatus에서 다음 단계로 이동하면 COMPLETION 상태가 된다.")
    void meal() {
        assertThat(OrderStatus.MEAL.transitionTo(OrderStatus.COMPLETION))
                .isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("COMPLETION 상태의 OrderStatus에서 다른 단계로 이동하려 하면 예외가 발생한다.")
    void completion() {
        assertThatThrownBy(() -> OrderStatus.COMPLETION.transitionTo(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

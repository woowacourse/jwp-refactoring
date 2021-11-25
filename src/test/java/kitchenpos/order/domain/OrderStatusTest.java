package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.exception.InvalidOrderStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderStatus 단위 테스트")
class OrderStatusTest {

    @DisplayName("주어진 OrderStatus가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void notFoundException() {
        // given
        assertThat(OrderStatus.findByName("COOKING")).isEqualTo(COOKING);
        assertThat(OrderStatus.findByName("MEAL")).isEqualTo(MEAL);
        assertThat(OrderStatus.findByName("COMPLETION")).isEqualTo(COMPLETION);

        // when, then
        assertThatThrownBy(() -> OrderStatus.findByName("호롤롤"))
            .isExactlyInstanceOf(InvalidOrderStatusException.class);
    }
}

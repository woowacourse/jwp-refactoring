package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    @DisplayName("OrderStatus로 변환이 가능하다.")
    void valueOf_success() {
        assertThat(OrderStatus.valueOf(OrderStatus.COMPLETION.name())).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("존재하지 않는 주문 상태가 전달되면 예외가 발생한다.")
    void valueOf_fail() {
        assertThatThrownBy(() -> OrderStatus.valueOf("kong"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

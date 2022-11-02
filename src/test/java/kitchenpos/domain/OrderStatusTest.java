package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @DisplayName("주문 상태 이름이 유효하지 않으면 예외가 발생한다.")
    @Test
    void findWithInvalidName() {
        assertThatThrownBy(() -> OrderStatus.find("abcd"))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("유효하지 않은 주문 상태입니다.");
    }
}

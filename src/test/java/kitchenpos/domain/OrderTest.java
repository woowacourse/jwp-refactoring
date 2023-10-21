package kitchenpos.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("현재 OrderStatus가 COMPLETION이면 Status를 변경할 수 없으므로 예외가 발생한다.")
    void validateAvailableChangeStatus() {
        // given
        final Order order = Order.from(ORDER_TABLE1());
        order.changeStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.validateAvailableChangeStatus())
                .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
    }
}

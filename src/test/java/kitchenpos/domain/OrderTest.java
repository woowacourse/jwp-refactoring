package kitchenpos.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시 주문 테이블이 비어있는 상태면 예외가 발생한다")
    void throws_OrderTableIsEmpty() {
        // given
        final OrderTable emptyOrderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true);

        // when & then
        assertThatThrownBy(() -> Order.from(emptyOrderTable))
                .isInstanceOf(OrderException.CannotOrderStateByOrderTableEmptyException.class)
                .hasMessage("[ERROR] 주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
    }

    @Test
    @DisplayName("OrderStatus 변경 시 현재 OrderStatus가 COMPLETION이면 Status를 변경할 수 없으므로 예외가 발생한다.")
    void validateAvailableChangeStatus() {
        // given
        final Order order = Order.from(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false));
        order.changeStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
    }
}

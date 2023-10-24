package kitchenpos.order.domain;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.order.OrderStatus;
import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시 요청한 주문 항목 목록의 개수와 메뉴 아이디로 조회한 메뉴의 개수가 다르면 예외가 발생한다.")
    void throws_notSameOrderLineItemsSize() {
        // given
        final int orderLineItemSize = 1;
        final int foundMenuSize = 2;

        // when & then
        assertThatThrownBy(() -> Order.from(1L, orderLineItemSize, foundMenuSize))
                .isInstanceOf(OrderException.NotFoundOrderLineItemMenuExistException.class)
                .hasMessage("[ERROR] 주문 항목 목록에 메뉴가 누락된 주문 항목이 존재합니다.");
    }

    @Test
    @DisplayName("OrderStatus 변경 시 현재 OrderStatus가 COMPLETION이면 Status를 변경할 수 없으므로 예외가 발생한다.")
    void validateAvailableChangeStatus() {
        // given
        final int orderLineItemSize = 1;
        final Order order = Order.from(new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, false).getId(), orderLineItemSize, orderLineItemSize);
        order.changeStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(() -> order.changeStatus(OrderStatus.MEAL))
                .isInstanceOf(OrderException.CannotChangeOrderStatusByCurrentOrderStatusException.class)
                .hasMessage("[ERROR] 기존 주문의 주문 상태가 계산 완료인 주문은 상태를 변경할 수 없습니다.");
    }
}

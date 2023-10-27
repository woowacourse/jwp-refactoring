package kitchenpos.ordertable.domain;

import kitchenpos.BaseTest;
import kitchenpos.ordertable.exception.OrderTableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest extends BaseTest {

    private OrderValidator orderValidator = new TestOrderValidator();

    @Test
    void 주문_테이블은_테이블_그룹이_존재하면_주문_상태를_바꾸지_못한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, true);
        orderTable.changeTableGroupId(1L);

        // when, then
        Assertions.assertThatThrownBy(() -> orderTable.changeEmpty(false, orderValidator))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문_테이블에_포함된_주문_중_요리나_식사_상태의_주문이_있으면_주문_상태를_바꾸지_못한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);

        // when, then
        Assertions.assertThatThrownBy(() -> orderTable.changeEmpty(false, orderValidator))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문_테이블이_주문을_할_수_없는_상태면_게스트_수를_바꾸지_못한다() {
        // given
        OrderTable orderTable = new OrderTable(1, true);

        // when, then
        Assertions.assertThatThrownBy(() -> orderTable.changeNumberOfGuests(new GuestNumber(2)))
                .isInstanceOf(OrderTableException.class);
    }

    class TestOrderValidator implements OrderValidator {

        @Override
        public void validateOrder(Long orderTableId) {
            if (orderTableId == null) {
                throw new OrderTableException("주문테이블에 속한 주문이 요리중 또는 식사중이므로 상태를 변경할 수 없습니다.");
            }
        }
    }
}

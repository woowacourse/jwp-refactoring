package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderFixture.계산완료된_주문;
import static kitchenpos.domain.order.OrderFixture.조리중인_주문;
import static kitchenpos.exception.order.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;
import static kitchenpos.exception.order.OrderExceptionType.ORDER_LINE_ITEMS_CAN_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문항목이_비어있으면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Order(1L, null, new OrderLineItems(List.of()))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_LINE_ITEMS_CAN_NOT_EMPTY);
    }

    @Test
    void 주문테이블이_비어있지않고_주문항목이_비어있지않으면_예외가_발생하지_않는다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(null, 0);

        // when & then
        assertThatCode(() -> new Order(1L, null, new OrderLineItems(List.of(
                orderLineItem
        )))).doesNotThrowAnyException();
    }

    @Test
    void 주문상태가_조리중이거나_식사중이면_true를_반환한다() {
        // when
        boolean result = 조리중인_주문().isCookingOrMeal();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 주문상태가_조리중이거나_식사중이_아니면_false를_반환한다() {
        // when
        boolean result = 계산완료된_주문().isCookingOrMeal();

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 주문상태가_계산완료이면_주문상태를_변경할때_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                계산완료된_주문().changeOrderStatus(OrderStatus.MEAL)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS);
    }

    @Test
    void 주문상태가_계산완료가_아니면_주문상태를_변경한다() {
        // when
        Order 조리중인_주문 = 조리중인_주문();
        조리중인_주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(조리중인_주문.orderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}

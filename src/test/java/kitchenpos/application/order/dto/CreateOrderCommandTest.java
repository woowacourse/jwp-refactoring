package kitchenpos.application.order.dto;

import static kitchenpos.exception.order.OrderExceptionType.ORDER_LINE_ITEM_COMMANDS_CAN_NOT_NULL;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CreateOrderCommandTest {

    @Test
    void 주문_항목이_널이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new CreateOrderCommand(1L, null)
        ).exceptionType();

        // then
        Assertions.assertThat(exceptionType).isEqualTo(ORDER_LINE_ITEM_COMMANDS_CAN_NOT_NULL);
    }
}

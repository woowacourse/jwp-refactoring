package kitchenpos.application.table;

import static kitchenpos.order.exception.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.application.IntegrationTest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.event.OrderedEvent;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class OrderedEventListenerTest extends IntegrationTest {

    @Test
    void 주문테이블이_비어있으면_예외가_발생한다() {
        // given
        OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
        OrderedEvent orderedEvent = new OrderedEvent(주문테이블.id());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                orderedEventListener.validateTableIsNotEmpty(orderedEvent)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_EMPTY);
    }

    @Test
    void 주문테이블이_비어있지않으면_예외가_발생한다() {
        // given
        OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, false));
        OrderedEvent orderedEvent = new OrderedEvent(주문테이블.id());

        // when & then
        assertThatCode(() -> orderedEventListener.validateTableIsNotEmpty(orderedEvent))
                .doesNotThrowAnyException();
    }
}

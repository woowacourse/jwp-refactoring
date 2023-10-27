package kitchenpos.application.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.exception.order.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.application.IntegrationTest;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.event.TableEmptyChangedEvent;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class TableEmptyChangedEventListenerTest extends IntegrationTest {

    @Test
    void 조리중이거나_식사중인_주문이_있으면_예외가_발생한다() {
        // given
        OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
        주문저장(주문(주문테이블, COOKING, 주문항목(맛있는_메뉴_저장(), 2)));
        주문저장(주문(주문테이블, COMPLETION, 주문항목(맛있는_메뉴_저장(), 1)));
        TableEmptyChangedEvent tableEmptyChangedEvent = new TableEmptyChangedEvent(주문테이블.id());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableEmptyChangedEventListener.validateAllOrdersAreCompletion(tableEmptyChangedEvent)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_COOKING_OR_MEAL);
    }

    @Test
    void 조리중이거나_식사중인_주문이_없으면_예외가_발생하지_않는다() {
        // given
        OrderTable 주문테이블 = 주문테이블저장(주문테이블(0, true));
        주문저장(주문(주문테이블, COMPLETION, 주문항목(맛있는_메뉴_저장(), 2)));
        주문저장(주문(주문테이블, COMPLETION, 주문항목(맛있는_메뉴_저장(), 1)));
        TableEmptyChangedEvent tableEmptyChangedEvent = new TableEmptyChangedEvent(주문테이블.id());

        // when & then
        assertThatCode(() -> tableEmptyChangedEventListener.validateAllOrdersAreCompletion(tableEmptyChangedEvent))
                .doesNotThrowAnyException();
    }
}

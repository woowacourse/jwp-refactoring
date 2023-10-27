package kitchenpos.application.order;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.exception.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import kitchenpos.application.IntegrationTest;
import kitchenpos.event.TableUngroupedEvent;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;

class TableUngroupedEventListenerTest extends IntegrationTest {

    @Test
    void 조리중이거나_식사중인_주문이_있으면_예외가_발생한다() {
        // given
        OrderTable 주문테이블1 = 주문테이블저장(주문테이블(0, true));
        OrderTable 주문테이블2 = 주문테이블저장(주문테이블(0, true));
        주문저장(주문(주문테이블1, COOKING, 주문항목(맛있는_메뉴_저장(), 2)));
        주문저장(주문(주문테이블1, COMPLETION, 주문항목(맛있는_메뉴_저장(), 1)));
        주문저장(주문(주문테이블2, COMPLETION, 주문항목(맛있는_메뉴_저장(), 3)));
        TableUngroupedEvent tableUngroupedEvent = new TableUngroupedEvent(List.of(주문테이블1.id(), 주문테이블2.id()));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableUngroupedEventListener.validateAllOrdersAreCompletion(tableUngroupedEvent)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_COOKING_OR_MEAL);
    }

    @Test
    void 조리중이거나_식사중인_주문이_없으면_예외가_발생하지_않는다() {
        // given
        OrderTable 주문테이블1 = 주문테이블저장(주문테이블(0, true));
        OrderTable 주문테이블2 = 주문테이블저장(주문테이블(0, true));
        주문저장(주문(주문테이블1, COMPLETION, 주문항목(맛있는_메뉴_저장(), 2)));
        주문저장(주문(주문테이블1, COMPLETION, 주문항목(맛있는_메뉴_저장(), 1)));
        주문저장(주문(주문테이블2, COMPLETION, 주문항목(맛있는_메뉴_저장(), 3)));
        TableUngroupedEvent tableUngroupedEvent = new TableUngroupedEvent(List.of(주문테이블1.id(), 주문테이블2.id()));

        // when & then
        assertThatCode(() -> tableUngroupedEventListener.validateAllOrdersAreCompletion(tableUngroupedEvent))
                .doesNotThrowAnyException();
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.core.event.Events;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderStatusChangedEvent;
import kitchenpos.table.domain.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("주문 상태 변경됨 이벤트 핸들러의")
class OrderStatusChangedEventHandlerTest extends ServiceTest {

    @ParameterizedTest(name = "변경된 주문 상태 : {0}, 변경 전 테이블 상태 : {1}, 변경 후 테이블 상태 : {2}")
    @DisplayName("handle 메서드는 주문 상태 변경됨 이벤트가 발생하면 주문 상태가 변경된 테이블의 상태를 변경한다.")
    @CsvSource(value = {"COOKING,EMPTY,EAT_IN", "MEAL,EMPTY,EAT_IN", "COMPLETION,EAT_IN,EMPTY"})
    void handle(final OrderStatus orderStatus,
                final TableStatus beforeTableStatus,
                final TableStatus expected) {
        // given
        final Long orderTableId = saveOrderTable(10, false, beforeTableStatus)
                .getId();
        final OrderStatusChangedEvent event = new OrderStatusChangedEvent(orderTableId, orderStatus);

        // when
        Events.raise(event);
        final TableStatus actual = getOrderTable(orderTableId)
                .getTableStatus();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

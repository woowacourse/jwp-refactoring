package kitchenpos.ordertable.application;

import static kitchenpos.common.fixtures.OrderTableFixtures.ORDER_TABLE1_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.ServiceTest;
import kitchenpos.order.application.event.OrderCreateEvent;
import kitchenpos.order.exception.OrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableEventListenerTest extends ServiceTest {

    @Autowired
    private OrderTableEventListener orderTableEventListener;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("OrderCreateEvent가 발행되어 validateOrderTable인 이벤트 리슨 로직이 실행될 때")
    class ExecuteEventListener {

        @Test
        @DisplayName("OrderTableId에 해당하는 OrderTable이 존재하지 않아서 예외가 발생한다.")
        void throws_notExistOrderTable() {
            // given
            final Long notExistOrderTableId = -1L;
            final OrderCreateEvent orderCreateEvent = new OrderCreateEvent(notExistOrderTableId);

            // when & then
            assertThatThrownBy(() -> orderTableEventListener.validateOrderTable(orderCreateEvent))
                    .isInstanceOf(OrderTableException.NotFoundOrderTableException.class)
                    .hasMessage("[ERROR] 해당하는 OrderTable이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("OrderTableId에 해당하는 OrderTable이 비어 있으면 예외가 발생한다.")
        void throws_emptyOrderTable() {
            // given
            final OrderTable orderTable = new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, true);
            final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
            final OrderCreateEvent orderCreateEvent = new OrderCreateEvent(savedOrderTable.getId());

            // when & then
            assertThatThrownBy(() -> orderTableEventListener.validateOrderTable(orderCreateEvent))
                    .isInstanceOf(OrderException.CannotOrderStateByOrderTableEmptyException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있는 상태일 때 주문할 수 없습니다.");
        }
    }


}

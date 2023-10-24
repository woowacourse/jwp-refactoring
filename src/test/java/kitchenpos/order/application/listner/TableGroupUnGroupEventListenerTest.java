package kitchenpos.order.application.listner;

import static java.util.stream.Collectors.toUnmodifiableList;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.TableGroupUnGroupValidationEvent;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupUnGroupEventListenerTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupUnGroupEventListener eventListener;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Table의 주문 상태가 완료상태가 아닌경우 예외처리")
    void throwExceptionIfOrderIsNotCompletion() {
        //given
        final List<OrderTableDto> orderTableDtos = createOrderTableContainNoCompletion();

        final List<Long> tableGroupIds = orderTableDtos.stream()
            .map(OrderTableDto::getId)
            .collect(toUnmodifiableList());
        final TableGroupUnGroupValidationEvent event
            = new TableGroupUnGroupValidationEvent(tableGroupIds);

        //when
        assertThatThrownBy(() -> eventListener.handle(event))
            .isInstanceOf(OrderException.class)
            .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
    }

    private List<OrderTableDto> createOrderTableContainNoCompletion() {
        final OrderTable savedOrderTable = orderTableRepository.save(비어있는_주문_테이블());
        final OrderTable savedOrderTable2 = orderTableRepository.save(비어있는_주문_테이블());
        saveOrderMeal(savedOrderTable);

        return Stream.of(savedOrderTable, savedOrderTable2)
            .map(OrderTableDto::from)
            .collect(Collectors.toList());
    }

    private void saveOrderMeal(final OrderTable savedOrderTable) {
        final Order order = new Order(
            savedOrderTable.getId(),
            OrderStatus.MEAL,
            LocalDateTime.now(),
            Map.of()
        );
        orderRepository.save(order);
    }
}

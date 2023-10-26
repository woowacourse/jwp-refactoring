package kitchenpos.order.application.listner;

import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_COMPLETION;
import static kitchenpos.support.fixture.TableFixture.비어있는_주문_테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.support.ServiceIntegrationTest;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
import kitchenpos.table_group.application.dto.TableGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupOrderValidatorTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupOrderValidator eventListener;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Table의 주문 상태가 완료상태가 아닌경우 예외처리")
    void throwExceptionIfOrderIsNotCompletion() {
        //given
        final List<OrderTableDtoInTableGroup> orderTableDtos = createOrderTableContainNoCompletion();
        final TableGroupDto tableGroupDto = tableGroupService.create(
            new TableGroupDto(null, LocalDateTime.now(), orderTableDtos)
        );

        //when
        assertThatThrownBy(() -> eventListener.validateUngroup(tableGroupDto.getId()))
            .isInstanceOf(OrderException.class)
            .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
    }

    private List<OrderTableDtoInTableGroup> createOrderTableContainNoCompletion() {
        final OrderTable savedOrderTable = orderTableRepository.save(비어있는_주문_테이블());
        final OrderTable savedOrderTable2 = orderTableRepository.save(비어있는_주문_테이블());
        saveOrderMeal(savedOrderTable);

        return Stream.of(savedOrderTable, savedOrderTable2)
            .map(OrderTableDto::from)
            .map(ServiceIntegrationTest::map)
            .collect(Collectors.toList());
    }
}

package kitchenpos.order.application.listner;

import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.ORDER_IS_NOT_COMPLETION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.ServiceIntegrationTest;
import kitchenpos.fixture.TableFixture;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.implementation.TableGroupOrderValidator;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.application.TableGroupService;
import kitchenpos.table_group.application.dto.OrderTableDtoInTableGroup;
import kitchenpos.table_group.application.dto.TableGroupDto;
import kitchenpos.table_group.domain.exception.TableGroupException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupOrderValidatorTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupOrderValidator eventListener;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupService tableGroupService;
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
        final Long tableGroupId = tableGroupDto.getId();
        Assertions.assertThatThrownBy(() -> eventListener.validateUngroup(tableGroupId))
            .isInstanceOf(TableGroupException.class)
            .hasMessage(ORDER_IS_NOT_COMPLETION.getMessage());
    }

    private List<OrderTableDtoInTableGroup> createOrderTableContainNoCompletion() {
        final OrderTable savedOrderTable = orderTableRepository.save(TableFixture.비어있는_주문_테이블());
        final OrderTable savedOrderTable2 = orderTableRepository.save(TableFixture.비어있는_주문_테이블());
        saveOrderMeal(savedOrderTable);

        return Stream.of(savedOrderTable, savedOrderTable2)
            .map(OrderTableDto::from)
            .map(TableGroupOrderValidatorTest::map)
            .collect(Collectors.toList());
    }

    private void saveOrderMeal(final OrderTable savedOrderTable) { final Order order = new Order(
            savedOrderTable.getId(),
            OrderStatus.MEAL,
            LocalDateTime.now(),
            List.of()
        );
        orderRepository.save(order);
    }

    private static OrderTableDtoInTableGroup map(final OrderTableDto orderTableDto) {
        return new OrderTableDtoInTableGroup(
            orderTableDto.getId(),
            orderTableDto.getTableGroupId(),
            orderTableDto.getNumberOfGuests(),
            orderTableDto.getEmpty()
        );
    }
}

package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.TableDomainService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.exception.CannotChangeOrderTableEmpty;
import kitchenpos.table.exception.InvalidOrderTableException;
import kitchenpos.table.ui.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.table.ui.dto.response.OrderTableResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TableDomainServiceImpl implements TableDomainService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableDomainServiceImpl(
        OrderTableRepository orderTableRepository,
        OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderTableResponseDto changeEmpty(Long orderTableId,
        OrderTableEmptyRequestDto orderTableEmptyRequestDto) {
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderRepository.findByOrderTableId(orderTableId).ifPresent(this::validateIfCompleteOrder);
        orderTable.changeEmptyStatus(orderTableEmptyRequestDto.isEmpty());

        return new OrderTableResponseDto(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(InvalidOrderTableException::new);
    }

    private void validateIfCompleteOrder(Order order) {
        if (order.isNotCompletion()) {
            throw new CannotChangeOrderTableEmpty();
        }
    }
}

package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.dto.response.OrderTableResponseDto;
import kitchenpos.exception.CannotChangeOrderTableEmpty;
import kitchenpos.exception.InvalidOrderTableException;
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

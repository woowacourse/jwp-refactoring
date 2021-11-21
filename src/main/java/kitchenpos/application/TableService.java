package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.CannotChangeOrderTableEmpty;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.ui.dto.request.table.OrderTableRequestDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponseDto create(final OrderTableRequestDto orderTableRequestDto) {
        OrderTable created = orderTableRepository.save(new OrderTable(
            orderTableRequestDto.getNumberOfGuests(),
            orderTableRequestDto.getEmpty()
        ));

        return toOrderTableResponseDto(created);
    }

    public List<OrderTableResponseDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(this::toOrderTableResponseDto)
            .collect(toList());
    }

    private OrderTableResponseDto toOrderTableResponseDto(OrderTable orderTable) {
        return new OrderTableResponseDto(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    @Transactional
    public OrderTableResponseDto changeEmpty(final Long orderTableId,
        final OrderTableRequestDto orderTableRequestDto) {
        final OrderTable orderTable = findOrderTableById(orderTableId);

        orderRepository.findByOrderTableId(orderTableId).ifPresent(this::validateIfCompleteOrder);
        orderTable.changeEmptyStatus(orderTableRequestDto.getEmpty());

        return new OrderTableResponseDto(
            orderTable.getId(),
            orderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            orderTable.isEmpty()
        );
    }

    private void validateIfCompleteOrder(Order order) {
        if (order.isNotCompletion()) {
            throw new CannotChangeOrderTableEmpty();
        }
    }

    @Transactional
    public OrderTableResponseDto changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequestDto orderTableRequestDto) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequestDto.getNumberOfGuests());

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
}

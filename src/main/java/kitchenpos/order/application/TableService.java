package kitchenpos.order.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableCreationDto;
import kitchenpos.order.application.dto.OrderTableDto;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto createOrderTable(final OrderTableCreationDto orderTableCreationDto) {
        return OrderTableDto.from(orderTableRepository.save(OrderTableCreationDto.toEntity(orderTableCreationDto)));
    }

    @Transactional(readOnly = true)
    public List<OrderTableDto> getOrderTables() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final Boolean emptyStatus) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateChangeEmptyStatus(savedOrderTable);

        final OrderTable orderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), emptyStatus);

        return OrderTableDto.from(orderTableRepository.save(orderTable));
    }

    private void validateChangeEmptyStatus(final OrderTable orderTable) {
        final Optional<Order> order = orderRepository.findByTableId(orderTable.getId());
        if (orderTable.isPartOfTableGroup() || (order.isPresent() && !order.get().isInCompletionStatus())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return OrderTableDto.from(orderTableRepository.save(savedOrderTable.changeNumberOfGuest(numberOfGuests)));
    }
}

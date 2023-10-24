package kitchenpos.table.application;

import static kitchenpos.table.domain.exception.OrderTableExceptionType.ORDER_TABLE_IS_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.exception.OrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository,
        final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        final OrderTable orderTable = new OrderTable(
            orderTableDto.getNumberOfGuests(),
            orderTableDto.getEmpty()
        );

        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableDto.from(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableException(ORDER_TABLE_IS_NOT_FOUND));
        validateContainedTablesOrderStatusIsNotCompletion(orderTableId);
        savedOrderTable.changeEmpty(orderTableDto.getEmpty());
        return OrderTableDto.from(savedOrderTable);
    }

    private void validateContainedTablesOrderStatusIsNotCompletion(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId)
            .ifPresent(this::validateOrderIsNotCompletion);
    }

    private void validateOrderIsNotCompletion(final Order order) {
        if (order.isNotAlreadyCompletion()) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId,
        final OrderTableDto orderTableDto) {
        final int numberOfGuests = orderTableDto.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableException(ORDER_TABLE_IS_NOT_FOUND));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableDto.from(savedOrderTable);
    }
}

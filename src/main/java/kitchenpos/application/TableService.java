package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.request.PutOrderTableEmptyRequest;
import kitchenpos.dto.request.PutOrderTableGuestsNumberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final PutOrderTableEmptyRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(IllegalArgumentException::new);
        validateCompletion(orderTableId);
        validateTableGroupInvolve(savedOrderTable);
        savedOrderTable.setEmpty(orderTableRequest.getEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private void validateCompletion(final Long orderTableId) {
        final boolean isNotComplete = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, OrderStatus.notCompleteStatuses()
        );
        if (isNotComplete) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableGroupInvolve(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(
            final Long orderTableId,
            final PutOrderTableGuestsNumberRequest orderTableGuestsNumberRequest
    ) {
        final int numberOfGuests = orderTableGuestsNumberRequest.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }
}

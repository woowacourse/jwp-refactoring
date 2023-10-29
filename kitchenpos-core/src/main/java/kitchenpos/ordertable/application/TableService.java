package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.common.dto.request.CreateOrderTableRequest;
import kitchenpos.common.dto.request.PutOrderTableEmptyRequest;
import kitchenpos.common.dto.request.PutOrderTableGuestsNumberRequest;
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

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final PutOrderTableEmptyRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(IllegalArgumentException::new);
        validateCompletion(orderTableId);
        validateTableGroupInvolve(savedOrderTable);
        savedOrderTable.changeEmptyStatus(orderTableRequest.getEmpty());
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
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableRepository.save(savedOrderTable);
    }
}

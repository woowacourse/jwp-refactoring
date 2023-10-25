package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import kitchenpos.ui.dto.PutOrderTableEmptyRequest;
import kitchenpos.ui.dto.PutOrderTableGuestsNumberRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        final boolean isNotComplete = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, OrderStatus.notCompleteStatus()
        );
        if (isNotComplete) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.setEmpty(orderTableRequest.getEmpty());
        return orderTableRepository.save(savedOrderTable);
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

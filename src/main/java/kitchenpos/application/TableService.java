package kitchenpos.application;

import java.util.Arrays;
import javax.transaction.Transactional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeGuestRequest;
import kitchenpos.dto.table.OrderTableCreateRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.OrderTablesResponse;
import kitchenpos.exception.OrderTableException;
import kitchenpos.exception.OrderTableException.CannotChangeEmptyStateByOrderStatusException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTablesResponse list() {
        return OrderTablesResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableException.NotFoundOrderTableException::new);

        if (savedOrderTable.isExistTableGroup()) {
            throw new OrderTableException.AlreadyExistTableGroupException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotChangeEmptyStateByOrderStatusException();
        }

        savedOrderTable.changeEmptyStatus(request.getEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeGuestRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableException.NotFoundOrderTableException::new);

        savedOrderTable.validateAvailableChangeNumberOfGuests();
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }
}

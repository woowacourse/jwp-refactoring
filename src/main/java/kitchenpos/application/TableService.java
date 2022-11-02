package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableEmptyRequest;
import kitchenpos.dto.request.OrderTableNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exceptions.EntityNotExistException;
import kitchenpos.exceptions.OrderNotCompletionException;
import kitchenpos.exceptions.OrderTableAlreadyHasTableGroupException;
import kitchenpos.exceptions.OrderTableEmptyException;
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
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRequest.toEntity();
        orderTableRepository.save(orderTable);

        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityNotExistException::new);
        validateEmptyOrderTable(orderTable);
        validateOrderStatusCompletion(orderTableId);
        orderTable.updateEmptyStatus(orderTableEmptyRequest.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new OrderTableAlreadyHasTableGroupException();
        }
    }

    private void validateOrderStatusCompletion(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(COOKING, MEAL))) {
            throw new OrderNotCompletionException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestRequest) {
        final OrderTable orderTable = validateEmptyOrderTable(orderTableId);

        final NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableNumberOfGuestRequest.getNumberOfGuests());
        orderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }

    private OrderTable validateEmptyOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(EntityNotExistException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
        return orderTable;
    }
}

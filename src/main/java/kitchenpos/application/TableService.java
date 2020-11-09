package kitchenpos.application;

import kitchenpos.dto.table.OrderTableChangeEmptyRequest;
import kitchenpos.dto.table.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);

        validateGroupingTable(savedOrderTable);
        validateIsCompletionOrder(orderTableId);

        savedOrderTable.updateEmpty(orderTableChangeEmptyRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateGroupingTable(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsCompletionOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest) {
        final int numberOfGuests = orderTableChangeNumberOfGuestsRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = findById(orderTableId);

        validateNotEmptyTable(savedOrderTable);

        savedOrderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateNotEmptyTable(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

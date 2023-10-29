package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableNumberOfGuests;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(
                null,
                new OrderTableNumberOfGuests(request.getNumberOfGuests()),
                request.getEmpty()
        );
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return convertToResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.validateChangeEmpty();
        orderTableValidator.validateOrderStatus(orderTableId, Arrays.asList("COOKING", "MEAL"));
        orderTable.updateEmpty(request.getEmpty());
        return convertToResponse(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.validateIsEmpty();
        orderTable.updateNumberOfGuests(new OrderTableNumberOfGuests(request.getNumberOfGuests()));
        return convertToResponse(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private OrderTableResponse convertToResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}

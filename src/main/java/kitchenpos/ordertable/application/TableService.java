package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrdersInTableCompleteValidator ordersInTableCompleteValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrdersInTableCompleteValidator ordersInTableCompleteValidator,
                        final OrderTableRepository orderTableRepository) {
        this.ordersInTableCompleteValidator = ordersInTableCompleteValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(createOrderTableByRequest(request));
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable createOrderTableByRequest(final OrderTableCreateRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        validateOrderTableGroupExist(orderTable);
        validateOrderTableOrderStatuses(orderTable);
        orderTable.changeEmpty(request.isEmpty());

        final OrderTable updateOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(updateOrderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableGroupExist(final OrderTable orderTable) {
        if (orderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableOrderStatuses(final OrderTable orderTable) {
        ordersInTableCompleteValidator.validate(orderTable.getId());
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        final OrderTable updateOrderTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(updateOrderTable);
    }
}

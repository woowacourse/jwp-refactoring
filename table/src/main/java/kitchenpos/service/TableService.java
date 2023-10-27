package kitchenpos.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.event.ValidateAllOrderCompletedEvent;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.ChangeEmptyTableRequest;
import kitchenpos.dto.request.ChangeTableGuestRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.OrderTableCountNotEnoughException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private static final int ORDER_TABLE_COUNT_LOWER_LIMIT_WHEN_GROUPING = 2;

    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;

    public TableService(ApplicationEventPublisher eventPublisher, OrderTableRepository orderTableRepository) {
        this.eventPublisher = eventPublisher;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(CreateOrderTableRequest request) {
        OrderTable orderTable = new OrderTable(null, request.getNumberOfGuest(), request.getEmpty());
        orderTableRepository.save(orderTable);

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> findAll() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, ChangeEmptyTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        eventPublisher.publishEvent(new ValidateAllOrderCompletedEvent(orderTable.getId()));
        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, ChangeTableGuestRequest request) {
        int numberOfGuests = request.getNumberOfGuests();

        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public void ungroupByTableGroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateAllOrdersCompleted(orderTables);
        unGroupOrderTables(orderTables);
    }

    private void validateAllOrdersCompleted(List<OrderTable> orderTables) {
        orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList())
                .forEach(each -> eventPublisher.publishEvent(new ValidateAllOrderCompletedEvent(each)));
    }

    private void unGroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    @Transactional
    public void groupOrderTables(Long tableGroupId, List<Long> orderTableIds) {
        List<OrderTable> orderTables = findOrderTables(orderTableIds);
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(tableGroupId);
        }
        validateOrderTableCount(orderTables);
    }

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != orderTableIds.size()) {
            throw new OrderTableNotFoundException();
        }
        return orderTables;
    }

    private void validateOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables.size() < ORDER_TABLE_COUNT_LOWER_LIMIT_WHEN_GROUPING) {
            throw new OrderTableCountNotEnoughException();
        }
    }

    public void validateNotEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
        orderTable.validateIsNotEmpty();
    }
}

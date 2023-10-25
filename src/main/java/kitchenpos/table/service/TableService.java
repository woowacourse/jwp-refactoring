package kitchenpos.table.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.event.ValidateAllOrderCompletedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.ChangeEmptyTableRequest;
import kitchenpos.table.dto.request.ChangeTableGuestRequest;
import kitchenpos.table.dto.request.CreateOrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.exception.OrderTableCountNotEnoughException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

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
            orderTable.changeTableGroup(null);
        }
    }

    @Transactional
    public void groupOrderTables(Long tableGroupId, List<Long> orderTableIds) {
        List<OrderTable> orderTables = findOrderTables(orderTableIds);
        for (OrderTable orderTable : orderTables) {
            changeOrderTableGroup(tableGroupId, orderTable);
        }
        validateOrderTableCount(orderTables);
    }

    private void changeOrderTableGroup(Long tableGroupId, OrderTable orderTable) {
        orderTable.validateIsEmpty();
        orderTable.validateTableGroupNotExists();

        orderTable.changeEmpty(false);
        orderTable.changeTableGroup(tableGroupId);
    }

    private List<OrderTable> findOrderTables(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(each -> orderTableRepository.findById(each)
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(Collectors.toList());
    }

    private void validateOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new OrderTableCountNotEnoughException();
        }
    }
}

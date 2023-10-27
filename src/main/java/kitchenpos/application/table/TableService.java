package kitchenpos.application.table;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableChangedEmptyEvent;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableDao orderTableDao;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableService(final OrderTableDao orderTableDao, final ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableDao = orderTableDao;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable newOrderTable = new OrderTable(orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty());
        OrderTable savedOrderTable = orderTableDao.save(newOrderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> allOrderTables = orderTableDao.findAll();
        return allOrderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateResetToEmptyRequest(orderTableRequest);

        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateGroupedOrderTable(savedOrderTable);

        savedOrderTable.changeToEmptyTable();
        savedOrderTable = orderTableDao.save(savedOrderTable);

        applicationEventPublisher.publishEvent(new OrderTableChangedEmptyEvent(orderTableId));
        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateResetToEmptyRequest(OrderTableRequest orderTableRequest) {
        if (!orderTableRequest.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateGroupedOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        savedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(savedOrderTable);
    }
}

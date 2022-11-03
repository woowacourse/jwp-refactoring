package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderCompletedEvent;
import kitchenpos.order.domain.OrderProceededEvent;
import kitchenpos.order.dto.request.EmptyRequest;
import kitchenpos.order.dto.request.NumberOfGuestsRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(
            final OrderTableDao orderTableDao
    ) {
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        return OrderTableResponse.of(orderTableDao.save(request.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final EmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        final OrderTable changedOrderTable = savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableDao.save(changedOrderTable));
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        final OrderTable changedOrderTable = savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableDao.save(changedOrderTable));
    }

    @Async
    @EventListener
    @Transactional
    public void ordered(final OrderProceededEvent event) {
        final OrderTable orderTable = orderTableDao.findById(event.getOrderTableId())
                .orElseThrow(IllegalStateException::new);
        final OrderTable ordered = orderTable.order();

        orderTableDao.save(ordered);
    }

    @Async
    @EventListener
    @Transactional
    public void completedOrder(final OrderCompletedEvent event) {
        final OrderTable orderTable = orderTableDao.findById(event.getOrderTableId())
                .orElseThrow(IllegalStateException::new);
        final OrderTable completed = orderTable.completedOrder();

        orderTableDao.save(completed);
    }
}

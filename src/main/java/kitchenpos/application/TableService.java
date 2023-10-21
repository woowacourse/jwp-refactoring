package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final var orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final var orderTable = findById(orderTableId);

        orderTable.changeEmpty(request.isEmpty());

        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final var orderTable = findById(orderTableId);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                            .orElseThrow(EntityNotFoundException::new);
    }
}

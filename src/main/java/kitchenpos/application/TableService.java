package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.ordertable.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTableRequest) {
        final OrderTable orderTable = new OrderTable(
                null,
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeEmpty(orderTable.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTableRequest) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return orderTableDao.save(orderTable);
    }
}

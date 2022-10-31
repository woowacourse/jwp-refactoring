package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableDao.save(request.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableDao.getById(orderTableId);
        orderTable.changeEmpty(request.getEmpty());

        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableDao.getById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }
}

package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderValidator orderValidator;

    public TableService(final OrderTableDao orderTableDao, final OrderValidator orderValidator) {
        this.orderTableDao = orderTableDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderTable create(final OrderTable request) {
        return orderTableDao.save(request.init());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        orderValidator.ValidateOrderTableId(orderTableId);
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        return orderTableDao.save(savedOrderTable.updateEmpty(orderTable.isEmpty()));
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        return orderTableDao.save(savedOrderTable.updateNumberOfGuests(orderTable.getNumberOfGuests()));
    }
}

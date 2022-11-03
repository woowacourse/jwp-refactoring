package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderStatus.COOKING;
import static kitchenpos.table.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;

    public TableService(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
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
        validateOrderTableIdAndOrderStatusIn(orderTableId);
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        return orderTableDao.save(savedOrderTable.updateEmpty(orderTable.isEmpty()));
    }

    private void validateOrderTableIdAndOrderStatusIn(final Long orderTableId) {
        if (orderTableDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
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

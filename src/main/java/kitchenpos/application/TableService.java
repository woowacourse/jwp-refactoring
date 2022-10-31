package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderTable create(final OrderTable request) {
        return orderTableDao.save(OrderTable.create(request));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        return orderTableDao.save(savedOrderTable.updateEmpty(orderTable.isEmpty()));
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        validateTableGroupId(savedOrderTable);
        validateOrderTableIdAndOrderStatus(orderTableId);
        return savedOrderTable;
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableGroupId(final OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIdAndOrderStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        validateNumberOfGuests(orderTable);
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        validateEmpty(savedOrderTable);
        return orderTableDao.save(savedOrderTable.updateNumberOfGuests(orderTable.getNumberOfGuests()));
    }

    private static void validateNumberOfGuests(final OrderTable orderTable) {
        if (orderTable.isNegativeNumberOfGuests()) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

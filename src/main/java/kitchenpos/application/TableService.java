package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.request.OrderTableCreateRequest;
import kitchenpos.request.TableChangeEmptyRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuest(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, TableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateOrderStatus(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDao.save(savedOrderTable);
    }
}

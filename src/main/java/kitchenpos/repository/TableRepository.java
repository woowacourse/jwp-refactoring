package kitchenpos.repository;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderTableException;
import org.springframework.stereotype.Component;

@Component
public class TableRepository {

    private final OrderTableDao orderTableDao;

    public TableRepository(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public OrderTable save(OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> findAll() {
        return orderTableDao.findAll();
    }

    public OrderTable changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeEmpty(empty);

        return orderTableDao.save(savedOrderTable);
    }

    public OrderTable changeNumberOfGuests(Long orderTableId, Integer numberOfGuests) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
    }
}

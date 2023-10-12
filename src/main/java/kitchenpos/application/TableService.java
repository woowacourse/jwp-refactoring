package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.exception.TableServiceException.EmptyTableException;
import kitchenpos.application.exception.TableServiceException.ExistsNotCompletionOrderException;
import kitchenpos.application.exception.TableServiceException.ExistsTableGroupException;
import kitchenpos.application.exception.TableServiceException.InvalidNumberOfGuestsException;
import kitchenpos.application.exception.TableServiceException.NotExistsOrderTableException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new NotExistsOrderTableException(orderTableId));

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new ExistsTableGroupException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new ExistsNotCompletionOrderException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException(numberOfGuests);
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new NotExistsOrderTableException(orderTableId));

        if (savedOrderTable.isEmpty()) {
            throw new EmptyTableException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}

package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.application.response.TableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.ordertable.NumberOfGuests;
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
    public TableResponse create(final NumberOfGuests numberOfGuests, boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return TableResponse.from(orderTableDao.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(new OrderTables(orderTableDao.findAll()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableDao.findMandatoryById(orderTableId);
        validateOrderStatus(orderTableId);
        savedOrderTable.changeEmptyStatus(empty);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuests numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findMandatoryById(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}

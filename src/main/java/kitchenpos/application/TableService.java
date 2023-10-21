package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.response.OrderTableResponse;
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
    public Long create(final int numberOfGuests, boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return orderTableDao.save(orderTable).getId();
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableDao.findMandatoryById(orderTableId);
        validateOrderStatus(orderTableId);
        savedOrderTable.setEmpty(empty);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findMandatoryById(orderTableId);
        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}

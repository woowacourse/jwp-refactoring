package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderTableGroupDao orderTableGroupDao;

    public OrderTableService(OrderDao orderDao, OrderTableDao orderTableDao,
                             OrderTableGroupDao orderTableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderTableGroupDao = orderTableGroupDao;
    }

    @Transactional
    public OrderTable create(int numberOfGuests, boolean empty) {
        return orderTableDao.save(new OrderTable(numberOfGuests, empty));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId) {
        OrderTable orderTable = search(orderTableId);
        orderTableGroupDao.findById(orderTable.getOrderTableGroup().getId())
                .orElseThrow(IllegalArgumentException::new);

        validateIsCompletion(orderTable);
        orderTable.changeEmpty();
        return orderTableDao.save(orderTable);
    }

    private void validateIsCompletion(OrderTable orderTable) {
        List<Order> orders = orderDao.findByOrderTableId(orderTable.getId());
        if (orders.stream()
                .noneMatch(Order::isCompletion)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = search(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDao.save(orderTable);
    }

    public OrderTable search(long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

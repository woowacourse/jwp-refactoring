package kitchenpos.legacy.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.legacy.dao.OrderDao;
import kitchenpos.legacy.dao.OrderTableDao;
import kitchenpos.legacy.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyTableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public LegacyTableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTable create(OrderTable orderTable) {
        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> findAll() {
        return orderTableDao.findAll();
    }

    public OrderTable changeEmpty(Long orderTableId, OrderTable orderTable) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    public OrderTable changeNumberOfGuests(Long orderTableId, OrderTable orderTable) {
        int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}

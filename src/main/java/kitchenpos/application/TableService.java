package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.OrderTableEmptyRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()));

        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public void changeEmpty(Long orderTableId, OrderTableEmptyRequest orderTableEmptyRequest) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderTableDao.save(
                new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                        orderTableEmptyRequest.getEmpty()));
    }

    @Transactional
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

package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTable create(final OrderTableCreateRequest orderTable) {
        OrderTable orderTableInput = new OrderTable(null, null, orderTable.getNumberOfGuests(), orderTable.isEmpty());
        return orderTableDao.save(orderTableInput);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(orderTable);
    }
}

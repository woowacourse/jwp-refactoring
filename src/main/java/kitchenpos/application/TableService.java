package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.ordertable.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.ordertable.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private static final List<String> UNCHANGEABLE_ORDER_STATUSES = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableDao.save(createOrderTableByRequest(request));
    }

    private OrderTable createOrderTableByRequest(final OrderTableCreateRequest request) {
        return new OrderTable(request.getNumberOfGuests(), request.isEmpty());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        validateOrderTableGroupExist(orderTable);
        validateOrderTableOrderStatuses(orderTable);
        orderTable.changeEmpty(request.isEmpty());

        return orderTableDao.save(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderTableGroupExist(final OrderTable orderTable) {
        if (orderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableOrderStatuses(final OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), UNCHANGEABLE_ORDER_STATUSES)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }
}

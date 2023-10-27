package kitchenpos.application.table;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableReseponse;
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
    public OrderTableReseponse create(final OrderTableRequest orderTableRequest) {
        OrderTable newOrderTable = new OrderTable(orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty());
        OrderTable savedOrderTable = orderTableDao.save(newOrderTable);
        return OrderTableReseponse.from(savedOrderTable);
    }

    public List<OrderTableReseponse> list() {
        List<OrderTable> allOrderTables = orderTableDao.findAll();
        return allOrderTables.stream()
                .map(OrderTableReseponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableReseponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateResetToEmptyRequest(orderTableRequest);

        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateGroupedOrderTable(savedOrderTable);
        validateOrderCompletionByOrderTableId(orderTableId);

        savedOrderTable.changeToEmptyTable();
        savedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableReseponse.from(savedOrderTable);
    }

    private void validateResetToEmptyRequest(OrderTableRequest orderTableRequest) {
        if (!orderTableRequest.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateGroupedOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderCompletionByOrderTableId(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableReseponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        savedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableReseponse.from(savedOrderTable);
    }
}

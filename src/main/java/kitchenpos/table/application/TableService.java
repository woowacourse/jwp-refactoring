package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableDao.save(request.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId, request);
        final OrderTable updatedOrderTable = orderTableDao.save(savedOrderTable);

        return OrderTableResponse.from(updatedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableUpdateNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = getOrderTable(orderTableId, numberOfGuests);
        final OrderTable updatedOrderTable = orderTableDao.save(savedOrderTable);

        return OrderTableResponse.from(updatedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmpty();
        savedOrderTable.setNumberOfGuests(numberOfGuests);
        return savedOrderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateNotGrouping();
        validateCompletion(orderTableId);
        savedOrderTable.setEmpty(request.isEmpty());
        return savedOrderTable;
    }

    private void validateCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableEmptyRequest;
import kitchenpos.dto.request.OrderTableNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
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
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = orderTableRequest.toEntity();
        orderTableDao.save(orderTable);

        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        findNotEmptyOrderTable(orderTable);
        validateOrderStatusCompletion(orderTableId);
        orderTable.updateEmptyStatus(orderTableEmptyRequest.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    private void findNotEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatusCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestRequest) {
        final OrderTable orderTable = findNotEmptyOrderTable(orderTableId);

        final NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableNumberOfGuestRequest.getNumberOfGuests());
        orderTable.updateNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findNotEmptyOrderTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }
}

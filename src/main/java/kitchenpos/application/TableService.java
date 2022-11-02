package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.orderTable.OrderTableChangeEmptyRequest;
import kitchenpos.dto.orderTable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.orderTable.OrderTableCreateRequest;
import kitchenpos.dto.orderTable.OrderTableResponse;
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

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = orderTableDao.save(
                new OrderTable(null, null, request.getNumberOfGuests(), request.isEmpty())
        );

        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTable(orderTableId);
        validateTableGroupId(orderTable);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        return OrderTableResponse.from(
                orderTableDao.save(
                        new OrderTable(
                                orderTableId,
                                orderTable.getTableGroupId(),
                                orderTable.getNumberOfGuests(),
                                request.isEmpty()
                        )
                )
        );
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    private void validateTableGroupId(OrderTable orderTable) {
        if (orderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        validateNumberOfGuests(request.getNumberOfGuests());
        final OrderTable orderTable = findOrderTable(orderTableId);
        validateEmpty(orderTable);

        return OrderTableResponse.from(
                orderTableDao.save(
                        new OrderTable(
                                orderTableId,
                                orderTable.getTableGroupId(),
                                request.getNumberOfGuests(),
                                orderTable.isEmpty()
                        )
                )
        );
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

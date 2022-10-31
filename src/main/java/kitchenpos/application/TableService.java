package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
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

    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableDao.save(request.toOrder());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        final List<OrderTableResponse> tableResponses = orderTables.stream().map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return tableResponses;
    }

    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        validTableGroupCondition(savedOrderTable);
        validExistOrderTables(orderTableId);
        savedOrderTable.ClearTable();
        orderTableDao.save(savedOrderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validExistOrderTables(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }

    private void validTableGroupCondition(final OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new CustomIllegalArgumentException(INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION);
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }
}

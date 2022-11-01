package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableSaveRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        validateTableGroupNonNull(savedOrderTable);
        validateStatusNonCompletion(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return new OrderTableResponse(savedOrderTable);
    }

    private void validateStatusNonCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableGroupNonNull(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return new OrderTableResponse(savedOrderTable);
    }
}

package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableChangeStatusRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        return OrderTableResponse.from(orderTableDao.save(request.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeStatusRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);

        validateCompletionStatus(orderTableId);
        savedOrderTable.setEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateOrderTableEmpty(savedOrderTable);
        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTableById(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        return savedOrderTable;
    }

    private void validateCompletionStatus(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableEmpty(final OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

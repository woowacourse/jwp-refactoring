package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = generateOrderTable(orderTableCreateRequest);
        orderTable = orderTableDao.save(orderTable);
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    private OrderTable generateOrderTable(OrderTableCreateRequest orderTableCreateRequest) {
        return new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.getEmpty());
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(each -> new OrderTableResponse(each.getId(), each.getTableGroupId(), each.getNumberOfGuests(), each.isEmpty()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest) {
        OrderTable savedOrderTable = searchOrderTable(orderTableId);

        savedOrderTable = savedOrderTable.changeEmpty(orderTableUpdateEmptyRequest.getEmpty());

        savedOrderTable = orderTableDao.save(savedOrderTable);
        return new OrderTableResponse(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), savedOrderTable.isEmpty());
    }

    private OrderTable searchOrderTable(Long orderTableId) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateNonGroupedOrderTable(savedOrderTable);
        validateOrderStatusCompletion(orderTableId);
        return savedOrderTable;
    }

    private void validateOrderStatusCompletion(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNonGroupedOrderTable(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                           OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest) {
        OrderTable savedOrderTable = searchOrderTableWhenChange(orderTableId);

        savedOrderTable = savedOrderTable.changeNumberOfGuests(orderTableUpdateGuestsRequest.getNumberOfGuests());
        return new OrderTableResponse(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), savedOrderTable.isEmpty());
    }

    private OrderTable searchOrderTableWhenChange(Long orderTableId) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateNotEmpty(savedOrderTable);
        return savedOrderTable;
    }

    private void validateNotEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.OrderTableUpdateGuestsRequest;
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
    public OrderTable create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = generateOrderTable(orderTableCreateRequest);
        return orderTableDao.save(orderTable);
    }

    private OrderTable generateOrderTable(OrderTableCreateRequest orderTableCreateRequest) {
        validateNumberOfGuests(orderTableCreateRequest);
        return new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.getEmpty());
    }

    private void validateNumberOfGuests(OrderTableCreateRequest orderTableCreateRequest) {
        Integer numberOfGuests = orderTableCreateRequest.getNumberOfGuests();
        if (numberOfGuests == null || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest) {
        OrderTable savedOrderTable = searchOrderTable(orderTableId);

        savedOrderTable = savedOrderTable.changeEmpty(orderTableUpdateEmptyRequest.getEmpty());

        return orderTableDao.save(savedOrderTable);
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
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest) {
        OrderTable savedOrderTable = searchOrderTable(orderTableId, orderTableUpdateGuestsRequest.getNumberOfGuests());

        savedOrderTable = savedOrderTable.changeNumberOfGuests(orderTableUpdateGuestsRequest.getNumberOfGuests());

        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable searchOrderTable(Long orderTableId, Integer numberOfGuests) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateNotEmpty(savedOrderTable, numberOfGuests);
        return savedOrderTable;
    }

    private void validateNotEmpty(OrderTable savedOrderTable, Integer numberOfGuests) {
        if (numberOfGuests == null || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

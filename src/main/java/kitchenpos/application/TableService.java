package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeGuestRequest;
import kitchenpos.dto.OrderTableRequest;
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
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        return orderTableDao.save(new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId,
                                  final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        final OrderTable savedOrderTable = findById(orderTableId);

        checkChangeableStatus(orderTableId);

        return orderTableDao.save(new OrderTable(savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                orderTableChangeEmptyRequest.isEmpty()));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeGuestRequest orderTableChangeGuestRequest) {
        final int numberOfGuests = orderTableChangeGuestRequest.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        return orderTableDao.save(new OrderTable(savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                numberOfGuests,
                savedOrderTable.isEmpty()));
    }

    private void checkChangeableStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findById(Long orderTableId) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        return savedOrderTable;
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTable;
    }
}

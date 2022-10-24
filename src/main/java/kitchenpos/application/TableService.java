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
        Long numberOfGuests = orderTableCreateRequest.getNumberOfGuests();
        if (numberOfGuests == null || numberOfGuests < 0L) {
            throw new IllegalArgumentException();
        }
        return orderTableDao.save(new OrderTable(numberOfGuests.intValue(), orderTableCreateRequest.getEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTableUpdateEmptyRequest.getEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest) {
        final Long numberOfGuests = orderTableUpdateGuestsRequest.getNumberOfGuests();

        if (numberOfGuests < 0L) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests.intValue());

        return orderTableDao.save(savedOrderTable);
    }
}

package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Table create(final Table table) {
        return orderTableDao.save(table);
    }

    public List<Table> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public Table changeEmpty(final Long orderTableId, final Table table) {
        final Table savedTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedTable.changeEmpty(table.isEmpty());

        return orderTableDao.save(savedTable);
    }

    @Transactional
    public Table changeNumberOfGuests(final Long orderTableId, final Table table) {
        final int numberOfGuests = table.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final Table savedTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedTable.changeNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedTable);
    }
}

package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final TableDao tableDao;

    public TableService(final OrderDao orderDao, final TableDao tableDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public Table create() {
        return tableDao.save(new Table());
    }

    public List<Table> list() {
        return tableDao.findAll();
    }

    @Transactional
    public Table changeEmpty(final Long tableId, final boolean empty) {
        final Table savedTable = tableDao.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByTableIdAndOrderStatusIn(
                tableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedTable.changeEmpty(empty);

        return tableDao.save(savedTable);
    }

    @Transactional
    public Table changeNumberOfGuests(final Long tableId, final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final Table savedTable = tableDao.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedTable.changeNumberOfGuests(numberOfGuests);

        return tableDao.save(savedTable);
    }
}

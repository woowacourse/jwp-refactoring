package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.Table;
import kitchenpos.exception.TableCannotChangeEmptyException;
import kitchenpos.exception.TableNotExistenceException;

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
                .orElseThrow(TableNotExistenceException::new);

        validateChangeableTable(savedTable);

        savedTable.changeEmpty(empty);
        return tableDao.save(savedTable);
    }

    private void validateChangeableTable(Table savedTable) {
        List<Order> foundOrders = orderDao.findByTableIds(Arrays.asList(savedTable.getId()));
        for (Order foundOrder : foundOrders) {
            if (foundOrder.hasInProgressStatus()) {
                throw new TableCannotChangeEmptyException();
            }
        }
    }

    @Transactional
    public Table changeNumberOfGuests(final Long tableId, final int numberOfGuests) {
        final Table savedTable = tableDao.findById(tableId)
                .orElseThrow(TableNotExistenceException::new);

        savedTable.changeNumberOfGuests(numberOfGuests);

        return tableDao.save(savedTable);
    }
}

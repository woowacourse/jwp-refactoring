package kitchenpos.table.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableDao;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyEditRequest;
import kitchenpos.table.dto.TableGuestEditRequest;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final TableDao tableDao;

    public TableService(final OrderDao orderDao, final TableDao tableDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public Long create(TableCreateRequest request) {
        Table table = request.toEntity();
        return tableDao.save(table).getId();
    }

    public List<Table> list() {
        return tableDao.findAll();
    }

    @Transactional
    public void editEmpty(final Long orderTableId, TableEmptyEditRequest request) {
        final Table savedTable = findOne(orderTableId);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedTable.changeEmpty(request.getEmpty());
        tableDao.save(savedTable);
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final TableGuestEditRequest request) {
        Table savedTable = findOne(orderTableId);
        savedTable.changeNumberOfGuests(request.getNumberOfGuests());
        tableDao.save(savedTable);
    }

    private Table findOne(Long orderTableId) {
        return tableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

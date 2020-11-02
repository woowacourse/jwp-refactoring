package kitchenpos.table.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyEditRequest;
import kitchenpos.table.dto.TableGuestEditRequest;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderDao orderDao;
    private final TableRepository tableRepository;

    public TableService(OrderDao orderDao, TableRepository tableRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Long create(TableCreateRequest request) {
        Table table = request.toEntity();
        return tableRepository.save(table).getId();
    }

    public List<Table> list() {
        return tableRepository.findAll();
    }

    @Transactional
    public void editEmpty(final Long orderTableId, TableEmptyEditRequest request) {
        final Table savedTable = findOne(orderTableId);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedTable.changeEmpty(request.getEmpty());
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final TableGuestEditRequest request) {
        Table savedTable = findOne(orderTableId);
        savedTable.changeNumberOfGuests(request.getNumberOfGuests());
    }

    private Table findOne(Long orderTableId) {
        return tableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

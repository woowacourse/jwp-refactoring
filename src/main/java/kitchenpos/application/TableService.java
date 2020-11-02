package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.dto.TableChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final TableDao tableDao;

    public TableService(final OrderDao orderDao, final TableDao tableDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public Table create(final TableCreateRequest orderTable) {
        return tableDao.save(orderTable.toEntity());
    }

    public List<Table> list() {
        return tableDao.findAll();
    }

    @Transactional
    public Table changeEmpty(final Long orderTableId, final TableChangeRequest request) {
        final Table savedTable = tableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedTable.isGrouped()) {
            throw new IllegalArgumentException();
        }
        if (orderDao.existsByTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedTable.changeEmpty(request.isEmpty());

        return tableDao.save(savedTable);
    }

    @Transactional
    public Table changeNumberOfGuests(final Long orderTableId, final TableChangeRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final Table savedTable = tableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        savedTable.changeNumberOfGuests(numberOfGuests);

        return tableDao.save(savedTable);
    }
}

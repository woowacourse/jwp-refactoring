package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dto.ChangeNumberOfQuestsCommand;
import kitchenpos.application.dto.ChangeTableEmptyCommand;
import kitchenpos.application.dto.CreateTableCommand;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    public OrderTable create(final CreateTableCommand command) {
        OrderTable table = command.toDomain();
        return orderTableDao.save(table);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final ChangeTableEmptyCommand request) {
        Long tableId = request.getTableId();
        final OrderTable savedOrderTable = orderTableDao.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeEmpty(request.getEmpty());

        // TODO: JPA 적용 뒤 OrderTable에서 함께 검증 수행
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                tableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final ChangeNumberOfQuestsCommand request) {
        final OrderTable foundTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        foundTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(foundTable);
    }
}

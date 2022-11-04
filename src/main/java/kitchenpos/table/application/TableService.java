package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.application.OrderStatusValidator;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderStatusValidator orderStatusValidator;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderStatusValidator orderStatusValidator, final OrderTableDao orderTableDao) {
        this.orderStatusValidator = orderStatusValidator;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(null, null,
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty());

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateAbleToChangeEmpty(orderTableId, savedOrderTable);

        final OrderTable newOrderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(), orderTableRequest.isEmpty());

        return orderTableDao.save(newOrderTable);
    }

    private void validateAbleToChangeEmpty(final Long orderTableId, final OrderTable savedOrderTable) {
        savedOrderTable.validateHasGroupId();
        if (orderStatusValidator.existsByIdAndStatusNotCompletion(orderTableId)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final OrderTable newOrderTable = new OrderTable(savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                orderTableRequest.getNumberOfGuests(),
                savedOrderTable.isEmpty());

        return orderTableDao.save(newOrderTable);
    }
}

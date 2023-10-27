package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.application.dto.TableResponse;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableDao orderTableDao;
    private final List<OrderTablesChangingEmptinessValidator> changingEmptinessValidators;

    public TableService(final OrderTableDao orderTableDao,
                        final List<OrderTablesChangingEmptinessValidator> changingEmptinessValidators) {
        this.orderTableDao = orderTableDao;
        this.changingEmptinessValidators = changingEmptinessValidators;
    }

    @Transactional
    public TableResponse create(final NumberOfGuests numberOfGuests, boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return TableResponse.from(orderTableDao.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(new OrderTables(orderTableDao.findAll()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable orderTable = orderTableDao.findMandatoryById(orderTableId);
        for (OrderTablesChangingEmptinessValidator changingEmptinessValidator : changingEmptinessValidators) {
            changingEmptinessValidator.validate(orderTable);
        }
        orderTable.changeEmptyStatus(empty);
        return OrderTableResponse.from(orderTableDao.save(orderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuests numberOfGuests) {
        final OrderTable savedOrderTable = orderTableDao.findMandatoryById(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}

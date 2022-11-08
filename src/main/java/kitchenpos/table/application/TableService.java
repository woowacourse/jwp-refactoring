package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.application.validator.TableChangeEmptyValidator;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final TableChangeEmptyValidator tableChangeEmptyValidator;

    public TableService(final OrderTableDao orderTableDao,
        final TableChangeEmptyValidator tableChangeEmptyValidator) {
        this.orderTableDao = orderTableDao;
        this.tableChangeEmptyValidator = tableChangeEmptyValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableDao.getById(orderTableId);
        tableChangeEmptyValidator.validate(orderTable);
        orderTable.changeEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = orderTableDao.getById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }
}

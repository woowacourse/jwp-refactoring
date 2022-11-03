package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.request.OrderTableRequest;
import kitchenpos.order.application.response.OrderTableResponse;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.application.validator.TableChangeEmptyValidator;

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
        return OrderTableResponse.fromAll(orderTableDao.findAll());
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

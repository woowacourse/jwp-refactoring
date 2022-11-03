package kitchenpos.application;

import static java.util.stream.Collectors.*;

import kitchenpos.application.validator.TableValidator;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableSaveRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final TableValidator tableValidator;

    public TableService(final OrderTableDao orderTableDao, final TableValidator tableValidator) {
        this.orderTableDao = orderTableDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return new OrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        savedOrderTable.changeEmpty(tableValidator.validate(savedOrderTable.getId()), request.isEmpty());
        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = orderTableDao.getById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return new OrderTableResponse(savedOrderTable);
    }
}

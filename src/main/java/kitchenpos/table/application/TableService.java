package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderTableValidator orderTableValidator;

    public TableService(
            OrderTableDao orderTableDao,
            OrderTableValidator orderTableValidator
    ) {
        this.orderTableDao = orderTableDao;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.setEmpty(orderTableValidator, request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.setNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }
}

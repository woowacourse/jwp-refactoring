package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.service.TableValidator;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableNumOfGuestRequest;
import kitchenpos.ui.dto.OrderTableStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableDao orderTableDao;
    private final TableValidator tableValidator;

    public TableService(final OrderTableDao orderTableDao, TableValidator tableValidator) {
        this.orderTableDao = orderTableDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableDao.save(request.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableStatusRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        tableValidator.validateOrderStatusOfTable(orderTable.getId());
        orderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableNumOfGuestRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        OrderTable updatedOrderTable = orderTable.changeNumOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(updatedOrderTable);
    }
}

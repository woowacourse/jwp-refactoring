package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import kitchenpos.table.application.dto.request.TableEmptyRequest;
import kitchenpos.table.application.dto.request.TableNumberOfGuestsRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderStatusChangeValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.InvalidTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderStatusChangeValidator orderStatusChangeValidator;

    public TableService(OrderTableDao orderTableDao, OrderStatusChangeValidator orderStatusChangeValidator) {
        this.orderTableDao = orderTableDao;
        this.orderStatusChangeValidator = orderStatusChangeValidator;
    }

    @Transactional
    public Long create(OrderTableRequest orderTableRequest) {
        OrderTable order = new OrderTable(null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return orderTableDao.save(order);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public void changeEmpty(Long orderTableId, TableEmptyRequest tableEmptyRequest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.updateEmpty(tableEmptyRequest.getEmpty(), orderStatusChangeValidator);
        orderTableDao.updateEmpty(orderTable.getId(), orderTable.isEmpty());
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new InvalidTableException("테이블이 존재하지 않습니다."));
    }

    @Transactional
    public void changeNumberOfGuests(Long orderTableId, TableNumberOfGuestsRequest tableNumberOfGuestsRequest) {
        OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.updateNumberOfGuests(tableNumberOfGuestsRequest.getNumberOfGuests());
        orderTableDao.updateNumberOfGuests(orderTable.getId(), orderTable.getNumberOfGuests());
    }
}

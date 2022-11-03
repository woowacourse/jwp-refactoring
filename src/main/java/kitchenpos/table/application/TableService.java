package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableSaveRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.dao.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderStatusValidator orderStatusValidator;
    private final OrderTableDao orderTableDao;

    public TableService(OrderStatusValidator orderStatusValidator, OrderTableDao orderTableDao) {
        this.orderStatusValidator = orderStatusValidator;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableResponse create(OrderTableSaveRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return OrderTableResponse.toResponse(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
            .map(OrderTableResponse::toResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, Boolean empty) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderStatusValidator.validateChangeEmpty(orderTableId);
        OrderTable updatedOrderTable = orderTable.changeEmpty(empty);
        orderTableDao.save(updatedOrderTable);
        return OrderTableResponse.toResponse(updatedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = findOrderTable(orderTableId);
        OrderTable updatedOrderTable = orderTable.changeNumberOfGuests(numberOfGuests);

        orderTableDao.save(updatedOrderTable);
        return OrderTableResponse.toResponse(updatedOrderTable);
    }

    private OrderTable findOrderTable(Long oderTableId) {
        return orderTableDao.findById(oderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}

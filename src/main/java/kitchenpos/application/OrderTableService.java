package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderTableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderTableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = OrderTable.builder()
            .empty(request.getEmpty())
            .numberOfGuests(request.getNumberOfGuests())
            .build();
        OrderTable savedTable = orderTableDao.save(orderTable);
        return OrderTableResponse.from(savedTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> tables = orderTableDao.findAll();
        return OrderTableResponse.listFrom(tables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(
        final Long orderTableId,
        final OrderTableRequest request
    ) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        validateOrderStatus(savedOrderTable);

        savedOrderTable.changeEmpty(request.getEmpty());

        OrderTable changedTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(changedTable);
    }

    private void validateOrderStatus(final OrderTable orderTable) {
        if (orderTable.isInProgress()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
        final Long orderTableId,
        final OrderTableRequest request
    ) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        OrderTable changedTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.from(changedTable);
    }
}

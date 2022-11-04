package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.dao.OrderTableDao;
import kitchenpos.table.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderStatusValidator orderStatusValidator;

    public TableService(final OrderTableDao orderTableDao, final OrderStatusValidator orderStatusValidator) {
        this.orderTableDao = orderTableDao;
        this.orderStatusValidator = orderStatusValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable request) {
        return OrderTableResponse.from(orderTableDao.save(request));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        orderStatusValidator.validateChangeEmpty(orderTableId);
        savedOrderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableDao.save(savedOrderTable));
    }
}

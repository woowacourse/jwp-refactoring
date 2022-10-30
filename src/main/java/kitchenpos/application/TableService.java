package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableConvertEmptyStatusException;
import kitchenpos.repository.TableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(OrderDao orderDao, OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable =
                new OrderTable(orderTableCreateRequest.getNumberOfGuests(), orderTableCreateRequest.getEmpty());

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        validateOrderTableStatus(orderTableId);

        OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeEmpty(orderTableChangeEmptyRequest.getEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrderTableStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderTableConvertEmptyStatusException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId,
                                           OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableChangeNumberOfGuestsRequest.getNumberOfGuests());

        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
    }
}

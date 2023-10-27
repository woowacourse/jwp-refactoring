package kitchenpos.ordertable.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.request.OrderTableCreateRequest;
import kitchenpos.ordertable.request.TableChangeEmptyRequest;
import kitchenpos.ordertable.request.TableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuest(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, TableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTableById(orderTableId);
        validateOrderStatus(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable getOrderTableById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                            .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(Long orderTableId, TableChangeNumberOfGuestsRequest request) {
        OrderTable savedOrderTable = getOrderTableById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }
}

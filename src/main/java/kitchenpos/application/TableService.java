package kitchenpos.application;

import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.UpdateOrderTableNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderTable create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final UpdateOrderTableEmptyRequest request) {
        final OrderTable persistOrderTable = orderTableDao.findById(orderTableId)
                                                          .orElseThrow(OrderTableNotFoundException::new);
        final List<Order> orders = orderDao.findAllByOrderTableId(orderTableId);
        persistOrderTable.changeEmptyStatus(orders, request.isEmpty());

        return persistOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(
            final Long orderTableId,
            final UpdateOrderTableNumberOfGuestsRequest request
    ) {
        final OrderTable persistOrderTable = orderTableDao.findById(orderTableId)
                                                          .orElseThrow(OrderTableNotFoundException::new);

        persistOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return persistOrderTable;
    }
}

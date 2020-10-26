package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.OrderTableRequest;
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
    public OrderTableDto create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return OrderTableDto.of(savedOrderTable);
    }

    public List<OrderTableDto> list() {
        return OrderTableDto.listOf(orderTableDao.findAll());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        List<Order> ordersByOrderTable = orderDao.findAllByOrderTable(savedOrderTable);

        savedOrderTable.changeEmpty(orderTable.isEmpty(), ordersByOrderTable);
        return OrderTableDto.of(savedOrderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableDto.of(savedOrderTable);
    }
}

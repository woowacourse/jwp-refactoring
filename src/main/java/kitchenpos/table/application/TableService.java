package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderDto;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableChangeEmptyRequest;
import kitchenpos.table.dto.TableChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.TableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final TableCreateRequest request) {
        return orderTableDao.save(request.toEntity());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableChangeEmptyRequest request) {
        validateOrderStatus(orderTableId);
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        if (request.isEmpty()) {
            savedOrderTable.empty();
        } else {
            savedOrderTable.fill();
        }
        return orderTableDao.save(savedOrderTable);
    }

    private void validateOrderStatus(final Long orderTableId) {
        final OrderDto savedOrder = orderDao.findByOrderTableId(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (OrderStatus.COOKING.isStatus(savedOrder.getOrderStatus()) ||
                OrderStatus.MEAL.isStatus(savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

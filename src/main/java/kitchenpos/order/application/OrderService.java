package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.ui.request.OrderRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final OrderTable orderTable = orderTableDao.getById(request.getOrderTableId());
        validateOrderTableNotEmpty(orderTable);

        return orderDao.save(new Order(orderTable, request.getOrderLineItems()));
    }

    private void validateOrderTableNotEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return savedOrder;
    }
}

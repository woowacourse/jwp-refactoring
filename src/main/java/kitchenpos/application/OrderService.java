package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.request.OrderRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        final OrderDao orderDao,
        final OrderTableDao orderTableDao
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order create(final OrderRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems();

        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        validateOrderTableNotEmpty(orderTable);

        final Order savedOrder = orderDao.save(new Order(orderTable.getId(), orderLineItems));

        savedOrder.setIdToOrderLineItems();
        return savedOrder;
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
        final Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(request.getOrderStatus());
        return savedOrder;
    }
}

package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableEmptyException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(OrderDao orderDao, OrderTableDao orderTableDao, OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(Order order, List<OrderLineItem> orderLineItems) {
        validateOrderTable(order.getOrderTableId());
        Order savedOrder = orderDao.save(order);

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderId, orderLineItems);

        return new Order(savedOrder, savedOrderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private List<OrderLineItem> saveOrderLineItems(Long orderId, List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> orderLineItemDao.save(new OrderLineItem(orderId, orderLineItem)))
                .collect(Collectors.toList());
    }

    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> new Order(order, orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    public Order changeOrderStatus(Long orderId, String orderStatus) {
        Order savedOrder = findOrder(orderId);
        savedOrder.changeOrderStatus(orderStatus);
        return orderDao.save(savedOrder);
    }

    private Order findOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(NotFoundOrderException::new);
    }
}

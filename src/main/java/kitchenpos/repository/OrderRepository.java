package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(final Order order) {
        final Order savedOrder = orderDao.save(order);
        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(savedOrder.getId(), order.getOrderLineItems());
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    public Order update(final Order order) {
        final Order savedOrder = orderDao.save(order);
        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(savedOrder.getId()));
        return savedOrder;
    }

    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id);
    }

    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds, final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }

    private List<OrderLineItem> saveOrderLineItems(final Long orderId, final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(orderLineItem -> orderLineItemDao.save(
                new OrderLineItem(orderId, orderLineItem.getMenuId(), orderLineItem.getQuantity())
            ))
            .collect(Collectors.toUnmodifiableList());
    }
}

package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Orders save(final Orders order) {
        final Orders savedOrder = orderDao.save(order);
        return new Orders(
            savedOrder.getId(),
            savedOrder.getOrderTableId(),
            OrderStatus.valueOf(savedOrder.getOrderStatus()),
            savedOrder.getOrderedTime(),
            saveOrderLineItems(savedOrder.getId(), order.getOrderLineItems())
        );
    }

    public Orders update(final Orders order) {
        final Orders savedOrder = orderDao.save(order);
        return createOrder(savedOrder);
    }

    public Optional<Orders> findById(final Long id) {
        return orderDao.findById(id);
    }

    public List<Orders> findAll() {
        final List<Orders> orders = orderDao.findAll();
        return orders.stream()
            .map(this::createOrder)
            .collect(Collectors.toUnmodifiableList());
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

    private Orders createOrder(final Orders order) {
        return new Orders(
            order.getId(),
            order.getOrderTableId(),
            OrderStatus.valueOf(order.getOrderStatus()),
            order.getOrderedTime(),
            orderLineItemDao.findAllByOrderId(order.getId())
        );
    }
}

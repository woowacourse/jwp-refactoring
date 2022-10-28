package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final OrderDao orderDao,
                           final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order add(final Order order) {
        final Order savedOrder = orderDao.save(order);

        final var savedOrderLineItems = order.getOrderLineItems()
                .stream()
                .map(orderLineItem -> {
                    orderLineItem.setOrderId(savedOrder.getId());
                    return orderLineItemDao.save(orderLineItem);
                })
                .collect(Collectors.toList());

        return new Order(
                savedOrder.getId(),
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                savedOrderLineItems
        );
    }

    public Order get(final Long id) {
        return orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> getAll() {
        final List<Order> orders = orderDao.findAll();

        return orders.stream()
                .map(order -> {
                    final var orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());
                    return new Order(
                            order.getId(),
                            order.getOrderTableId(),
                            order.getOrderStatus(),
                            order.getOrderedTime(),
                            orderLineItems);
                }).collect(Collectors.toList());
    }
}

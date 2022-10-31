package kitchenpos.dao;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(final Order entity) {
        final OrderDto savedOrder = orderDao.save(entity);
        final Order order = savedOrder.toEntity();

        final List<OrderLineItem> savedOrderLineItems = entity.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItem(order.getId(), orderLineItem))
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());

        order.addOrderLineItems(savedOrderLineItems);

        return order;
    }

    public Order update(final Order entity) {
        final OrderDto savedOrder = orderDao.save(entity);
        final Order order = savedOrder.toEntity();

        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(order.getId());

        order.addOrderLineItems(orderLineItems);

        return order;
    }

    public Order findById(final Long id) {
        return orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new)
                .toEntity();
    }

    public List<Order> findAll() {
        final List<OrderDto> savedOrders = orderDao.findAll();
        final List<Order> orders = savedOrders.stream()
                .map(OrderDto::toEntity)
                .collect(Collectors.toList());

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }
}

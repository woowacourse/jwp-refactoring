package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
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
        final Order savedOrder = orderDao.save(entity);

        final List<OrderLineItem> savedOrderLineItems = entity.getOrderLineItems().stream()
                .map(orderLineItem -> new OrderLineItem(savedOrder.getId(), orderLineItem))
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());

        return new Order(savedOrder, savedOrderLineItems);
    }

    public Order update(final Order entity) {
        final Order savedOrder = orderDao.save(entity);
        final List<OrderLineItem> orderLineItems = orderLineItemDao.findAllByOrderId(savedOrder.getId());

        return new Order(savedOrder, orderLineItems);
    }

    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id);
    }

    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }
}

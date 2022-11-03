package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final OrderDao orderDao, final OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    public Order save(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        final Order orderEntity = orderDao.save(order);

        return OrderFactory.create(orderEntity, toOrderLineItemEntities(orderEntity.getId(), orderLineItems));
    }

    private List<OrderLineItem> toOrderLineItemEntities(final Long orderId, final List<OrderLineItem> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : orderLineItems) {
            final OrderLineItem orderLineItemWithOrderId = new OrderLineItem(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getMenuName(),
                    orderLineItem.getMenuPrice(),
                    orderLineItem.getQuantity());

            savedOrderLineItems.add(orderLineItemDao.save(orderLineItemWithOrderId));
        }

        return savedOrderLineItems;
    }

    public Order getById(final Long id) throws IllegalArgumentException {
        final Order orderEntity = orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItemEntities = orderLineItemDao.findAllByOrderId(id);

        return OrderFactory.create(orderEntity, orderLineItemEntities);
    }

    public List<Order> findAll() {
        final List<Order> orderEntities = orderDao.findAll();

        return toOrders(orderEntities);
    }

    public List<Order> findByOrderTableId(final Long orderTableId) {
        final List<Order> orderEntities = orderDao.findByOrderTableId(orderTableId);

        return toOrders(orderEntities);
    }

    private List<Order> toOrders(final List<Order> orderEntities) {
        final List<Order> orders = new ArrayList<>();
        for (final Order orderEntity : orderEntities) {
            final List<OrderLineItem> orderLineItemEntities = orderLineItemDao.findAllByOrderId(orderEntity.getId());
            orders.add(OrderFactory.create(orderEntity, orderLineItemEntities));
        }
        return orders;
    }
}

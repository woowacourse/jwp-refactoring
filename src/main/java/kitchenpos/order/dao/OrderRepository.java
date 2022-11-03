package kitchenpos.order.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.jdbcTemplateOrderLineItemDao = jdbcTemplateOrderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order savedOrder = jdbcTemplateOrderDao.save(entity);
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : entity.getOrderLineItems()) {
            orderLineItem.setOrderId(savedOrder.getId());
            savedOrderLineItems.add(jdbcTemplateOrderLineItemDao.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
        return savedOrder;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        final Optional<Order> order = jdbcTemplateOrderDao.findById(id);
        final List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(id);
        order.ifPresent(it -> it.setOrderLineItems(orderLineItems));
        return order;
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = jdbcTemplateOrderDao.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public Order update(final Order entity) {
        final Order savedOrder = jdbcTemplateOrderDao.save(entity);
        final List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(savedOrder.getId());
        savedOrder.setOrderLineItems(orderLineItems);
        return savedOrder;
    }
}

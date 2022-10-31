package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

public interface OrderDao {

    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    Order update(Order savedOrder);
}

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

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}

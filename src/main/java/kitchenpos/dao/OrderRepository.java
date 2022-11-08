package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao orderDao;
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao orderDao, final JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order savedOrder = orderDao.save(entity);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(entity.getOrderLineItems(), savedOrder.getId());

        return new Order(savedOrder.getId(),
                savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                orderLineItems);
    }

    private List<OrderLineItem> getOrderLineItems(final List<OrderLineItem> orderLineItems, final Long orderId) {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(orderId, it.getMenuId(), it.getQuantity()))
                .map(orderLineItemDao::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return orderDao.findById(id);
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(order -> new Order(order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}

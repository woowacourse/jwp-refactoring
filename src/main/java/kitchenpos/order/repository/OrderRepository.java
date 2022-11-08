package kitchenpos.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.dao.JdbcTemplateOrderDao;
import kitchenpos.order.repository.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.order.repository.dao.OrderDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           final JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.jdbcTemplateOrderLineItemDao = jdbcTemplateOrderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order order = jdbcTemplateOrderDao.save(entity);
        final List<OrderLineItem> orderLineItems = mapToOrderLineItems(entity, order);
        return createOrder(order, orderLineItems);
    }

    private List<OrderLineItem> mapToOrderLineItems(final Order entity, final Order order) {
        return entity.getOrderLineItems()
                .stream()
                .map(orderLineItem -> mapToOrderLineItem(order, orderLineItem))
                .map(jdbcTemplateOrderLineItemDao::save)
                .collect(Collectors.toList());
    }

    private OrderLineItem mapToOrderLineItem(final Order order, final OrderLineItem orderLineItem) {
        return new OrderLineItem(
                order.getId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    private static Order createOrder(final Order order, final List<OrderLineItem> orderLineItems) {
        return Order.of(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItems
        );
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return jdbcTemplateOrderDao.findById(id)
                .map(order -> createOrder(order, jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId())));
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = jdbcTemplateOrderDao.findAll();
        return orders.stream()
                .map(order -> createOrder(order, jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
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

    @Override
    public List<Order> findByOrderTableIds(final List<Long> orderTableIds) {
        return jdbcTemplateOrderDao.findByOrderTableIds(orderTableIds);
    }

    @Override
    public List<Order> findByOrderTableId(final Long orderTableId) {
        return jdbcTemplateOrderDao.findByOrderTableId(orderTableId);
    }
}

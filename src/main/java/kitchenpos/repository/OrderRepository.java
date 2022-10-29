package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
        final List<OrderLineItem> orderLineItems = entity.getOrderLineItems()
                .stream()
                .map(orderLineItem -> new OrderLineItem(
                        order.getId(),
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()
                ))
                .map(jdbcTemplateOrderLineItemDao::save)
                .collect(Collectors.toList());
        return new Order(
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
                .map(order -> new Order(
                        order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId())
                ));
    }

    @Override
    public List<Order> findAll() {
        final List<Order> orders = jdbcTemplateOrderDao.findAll();
        return orders.stream()
                .map(order -> new Order(
                        order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        jdbcTemplateOrderLineItemDao.findAllByOrderId(order.getId())
                )).collect(Collectors.toList());
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

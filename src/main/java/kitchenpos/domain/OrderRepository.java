package kitchenpos.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.OrderDao;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao jdbcTemplateOrderDao;
    private final JdbcTemplateOrderLineItemDao orderLineItemDao;

    public OrderRepository(final JdbcTemplateOrderDao jdbcTemplateOrderDao,
                           final JdbcTemplateOrderLineItemDao orderLineItemDao) {
        this.jdbcTemplateOrderDao = jdbcTemplateOrderDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(final Order entity) {
        final Order savedOrder = jdbcTemplateOrderDao.save(entity);

        final Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = entity.getOrderLineItems().stream()
                .map(orderLineItem -> {
                    orderLineItem.setOrderId(orderId);
                    return orderLineItemDao.save(orderLineItem);
                }).collect(Collectors.toList());
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return jdbcTemplateOrderDao.findById(id)
                .map(order -> new Order(
                        order.getId(),
                        order.getOrderTableId(),
                        order.getOrderStatus(),
                        order.getOrderedTime(),
                        orderLineItemDao.findAllByOrderId(order.getId())
                ));

    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplateOrderDao.findAll();
        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }
        return orders;
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
